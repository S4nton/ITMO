#include "SeamCarver.h"

#include <algorithm>
#include <math.h>
#include <vector>

SeamCarver::SeamCarver(Image image)
    : m_image(std::move(image))
{
}

const Image & SeamCarver::GetImage() const
{
    return m_image;
}

size_t SeamCarver::GetImageWidth() const
{
    return m_image.m_table.size();
}

size_t SeamCarver::GetImageHeight() const
{
    return m_image.m_table[0].size();
}

double SeamCarver::GetPixelEnergy(size_t columnId, size_t rowId) const
{
    const size_t width = GetImageWidth();
    const size_t height = GetImageHeight();

    const size_t rightX = (columnId + 1) % width;
    const size_t leftX = (columnId - 1 + width) % width;

    const double rx = m_image.GetPixel(rightX, rowId).m_red - m_image.GetPixel(leftX, rowId).m_red;
    const double gx = m_image.GetPixel(rightX, rowId).m_green - m_image.GetPixel(leftX, rowId).m_green;
    const double bx = m_image.GetPixel(rightX, rowId).m_blue - m_image.GetPixel(leftX, rowId).m_blue;

    const double deltaX = rx * rx + gx * gx + bx * bx;

    const size_t downY = (rowId + 1) % height;
    const size_t topY = (rowId - 1 + height) % height;

    const double ry = m_image.GetPixel(columnId, downY).m_red - m_image.GetPixel(columnId, topY).m_red;
    const double gy = m_image.GetPixel(columnId, downY).m_green - m_image.GetPixel(columnId, topY).m_green;
    const double by = m_image.GetPixel(columnId, downY).m_blue - m_image.GetPixel(columnId, topY).m_blue;

    const double deltaY = ry * ry + gy * gy + by * by;

    const double energy = sqrt(deltaX + deltaY);

    return energy;
}

std::vector<std::vector<double>> transpose(const std::vector<std::vector<double>> & table)
{
    const size_t n = table[0].size();
    const size_t m = table.size();
    std::vector<std::vector<double>> new_table(n, std::vector<double>(m));

    for (size_t i = 0; i < n; i++) {
        for (size_t j = 0; j < m; j++) {
            new_table[i][j] = table[j][i];
        }
    }
    return new_table;
}

std::vector<std::vector<double>> SeamCarver::GetEnergyTable() const
{

    std::vector<std::vector<double>> energy_table(m_image.m_table.size(), std::vector<double>(m_image.m_table[0].size()));

    for (size_t i = 0; i < energy_table.size(); i++) {
        for (size_t j = 0; j < energy_table[i].size(); j++) {
            energy_table[i][j] = GetPixelEnergy(i, j);
        }
    }
    return energy_table;
}

SeamCarver::Seam SeamCarver::findSeam(const std::vector<std::vector<double>> & table) const
{
    const size_t width = table[0].size();
    const size_t height = table.size();

    std::vector<std::vector<std::pair<double, size_t>>> dp(height, std::vector<std::pair<double, size_t>>(width));

    for (size_t i = 0; i < height; i++) {
        for (size_t j = 0; j < width; j++) {
            if (i == 0) {
                dp[i][j].first = table[i][j];
                dp[i][j].second = 0;
            }
            else {
                double mn = dp[i - 1][j].first;
                size_t mnParent = j;

                if (j > 0 && mn > dp[i - 1][j - 1].first) {
                    mn = dp[i - 1][j - 1].first;
                    mnParent = j - 1;
                }

                if (j + 1 < width && mn > dp[i - 1][j + 1].first) {
                    mn = dp[i - 1][j + 1].first;
                    mnParent = j + 1;
                }

                dp[i][j].first = table[i][j] + mn;
                dp[i][j].second = mnParent;
            }
        }
    }

    double mn = dp[height - 1][0].first;
    size_t mnPos = 0;
    for (size_t j = 1; j < width; j++) {
        if (mn > dp[height - 1][j].first) {
            mn = dp[height - 1][j].first;
            mnPos = j;
        }
    }

    Seam result;
    size_t i = height - 1;
    size_t j = mnPos;

    while (i > 0) {
        result.push_back(j);
        j = dp[i][j].second;
        i--;
    }
    result.push_back(j);
    std::reverse(result.begin(), result.end());
    return result;
}

SeamCarver::Seam SeamCarver::FindVerticalSeam() const
{
    return findSeam(transpose(GetEnergyTable()));
}

SeamCarver::Seam SeamCarver::FindHorizontalSeam() const
{
    return findSeam(GetEnergyTable());
}

void SeamCarver::removeSeam(const Seam & seam, const bool isVertical)
{
    const size_t n = isVertical ? GetImageHeight() : GetImageWidth();
    const size_t m = isVertical ? GetImageWidth() : GetImageHeight();

    for (size_t i = 0; i < n; i++) {
        bool isMove = false;
        for (size_t j = 0; j < m; j++) {
            if (isMove) {
                if (!isVertical) {
                    m_image.m_table[i][j - 1] = m_image.m_table[i][j];
                }
                else {
                    m_image.m_table[j - 1][i] = m_image.m_table[j][i];
                }
            }
            else {
                isMove = j == seam[i];
            }
        }
    }

    if (!isVertical) {
        for (size_t i = 0; i < n; i++) {
            m_image.m_table[i].pop_back();
        }
    }
    else {
        m_image.m_table.pop_back();
    }
}

void SeamCarver::RemoveVerticalSeam(const Seam & seam)
{
    removeSeam(seam, true);
}

void SeamCarver::RemoveHorizontalSeam(const Seam & seam)
{
    removeSeam(seam, false);
}
"use strict";

const abstractOperation = operation => (...operands) => (...val) => operation(...operands.map(operand => operand(...val)));

const add = abstractOperation((a, b) => a + b);
const subtract = abstractOperation((a, b) => a - b);
const multiply = abstractOperation((a, b) => a * b);
const divide = abstractOperation((a, b) => a / b);
const negate = abstractOperation(a => -a);
const min5 = abstractOperation(Math.min);
const max3 = abstractOperation((...operands) => Math.max(...operands));

const variableName = ["x", "y", "z"];

const variable = name => (...val) => val[variableName.indexOf(name)];

const cnst = val => () => parseInt(val);

const one = cnst(1);
const two = cnst(2);

let notNumberConst = [];
notNumberConst["one"] = one;
notNumberConst["two"] = two;

const splitExpression = (expressionRPN) => {
    let elems = expressionRPN.trim().split(/\s+/);
    return elems;
}

let operations = [];
operations["+"] = add;
operations["-"] = subtract;
operations["*"] = multiply;
operations["/"] = divide;
operations["negate"] = negate;
operations["min5"] = min5;
operations["max3"] = max3;

let arity = [];
arity["+"] = 2;
arity["-"] = 2;
arity["*"] = 2;
arity["/"] = 2;
arity["negate"] = 1;
arity["min5"] = 5;
arity["max3"] = 3;


const parse = expressionRPN => {
    let elements = splitExpression(expressionRPN);
    let operands = [];
    elements.forEach(elem => {
        if (operations[elem] !== undefined) {
            let opernadsNow = [];
            for (let i = 0; i < arity[elem]; i++) {
                opernadsNow.push(operands.pop());
            }
            opernadsNow.reverse();
            operands.push(operations[elem](...opernadsNow));
        } else if (variableName.indexOf(elem) !== -1) {
            operands.push(variable(elem));
        } else if (notNumberConst[elem] !== undefined) {
            operands.push(notNumberConst[elem]);
        } else {
            operands.push(cnst(elem));
        }
    });
    return operands[0];
}

const checkingSolution = () => {
    for (let i = 0; i <= 10; i++) {
        let expr = add(subtract(multiply(variable("x"), variable("x")), multiply(variable("x"), cnst(2))), cnst(1));
        println(expr(i));
    }
}

//checkingSolution();
//println(parse("x x 2 - * x * 1 +")(5));
//println(add(one, variable("x")));
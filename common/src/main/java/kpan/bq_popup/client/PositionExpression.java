package kpan.bq_popup.client;

import java.util.Stack;
import java.util.function.DoubleBinaryOperator;
import kpan.bq_popup.util.StringReader;

public class PositionExpression {

    // 実装をサボる
    public static boolean canParse(String expression) {
        return !Double.isNaN(parseExpression(0, 0, expression));
    }

    public static double parseExpression(int screenSize, int iconSize, String expression) {
        // 操車場アルゴリズムと呼ぶらしい
        Stack<Double> valueStack = new Stack<>();
        Stack<Operation> operationStack = new Stack<>();

        StringReader stringReader = new StringReader(expression);

        while (stringReader.canRead()) {
            stringReader.skipWhitespace();
            if (stringReader.testAndSkip('+')) {
                if (!operationStack.isEmpty()) {
                    if (valueStack.size() < 2)
                        return Double.NaN;
                    Double a = valueStack.pop();
                    Double b = valueStack.pop();
                    valueStack.push(operationStack.pop().operate(a, b));
                }
                operationStack.add(Operation.ADD);
            } else if (stringReader.testAndSkip('-')) {
                if (!operationStack.isEmpty()) {
                    if (valueStack.size() < 2)
                        return Double.NaN;
                    Double a = valueStack.pop();
                    Double b = valueStack.pop();
                    valueStack.push(operationStack.pop().operate(a, b));
                }
                operationStack.add(Operation.SUB);
            } else {
                double value = stringReader.readDouble();
                if (Double.isNaN(value))
                    return Double.NaN;

                if (stringReader.testAndSkip('%')) {
                    value *= screenSize * 0.01;
                    // } else if (stringReader.testAndSkip('@')) {//一旦保留
                    //     value *= iconSize * 0.01;
                }
                valueStack.push(value);
            }
        }
        while (!operationStack.isEmpty()) {
            if (valueStack.size() < 2)
                return Double.NaN;
            Double a = valueStack.pop();
            Double b = valueStack.pop();
            valueStack.push(operationStack.pop().operate(a, b));
        }
        if (valueStack.isEmpty())
            return Double.NaN;
        return valueStack.pop();
    }

    private enum Operation {
        ADD((a, b) -> a + b),
        SUB((a, b) -> a - b),
        ;
        private final DoubleBinaryOperator operation;

        Operation(DoubleBinaryOperator operation) {
            this.operation = operation;
        }

        public double operate(double a, double b) {
            return operation.applyAsDouble(a, b);
        }
    }

}

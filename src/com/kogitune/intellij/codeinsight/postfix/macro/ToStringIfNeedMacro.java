package com.kogitune.intellij.codeinsight.postfix.macro;

import com.intellij.codeInsight.template.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

/**
 * Created by takam on 2015/05/05.
 */
public class ToStringIfNeedMacro extends Macro {


    public String getName() {
        return "to_string";
    }

    public String getPresentableName() {
        return "to_string";
    }

    @Nullable
    @Override
    public Result calculateResult(Expression[] expressions, ExpressionContext context) {
        if (expressions.length == 0) {
            return null;
        }


        Project project = context.getProject();
        final String exprText = expressions[0].calculateResult(context).toString();
        try {
            PsiElementFactory elementFactory = JavaPsiFacade.getElementFactory(project);
            final PsiExpression expression = elementFactory.createExpressionFromText(exprText, context.getPsiElementAtStartOffset());
            final PsiType type = expression.getType();

            if ("java.lang.String".equals(type.getCanonicalText())) {
                // example "test:" + test
                return new TextResult(exprText);
            }
            if (expression instanceof PsiPolyadicExpression) {
                // example 1 + 1
                return new TextResult("\"" + exprText + ":\" + (" + exprText + ")");
            }
            // example 1
            return new TextResult("\"" + exprText + ":\" + " + exprText);
        } catch (Exception e) {
            // ignored. because can use default result.
            e.printStackTrace();
        }
        return new TextResult(exprText);

    }
}

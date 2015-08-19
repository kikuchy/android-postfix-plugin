package com.kogitune.intellij.codeinsight.postfix.macro;

import com.intellij.codeInsight.template.*;
import com.intellij.codeInsight.template.macro.MacroUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * macro for android TextUtils.split.
 *
 * @author kikuchy
 */
public class SeparatorMacro extends Macro {

    public static final String FIELD_NAME_SEPARATOR = "SEPARATOR";
    public static final String FIELD_NAME_DELIMITER = "DELIMITER";
    public static final String FIELD_NAME_DIVIDER = "DIVIDER";

    public static final String DEFAULT_SEPARATOR = "\",\"";

    public String getName() {
        return "separator";
    }

    public String getPresentableName() {
        return "separator";
    }

    @Nullable
    @Override
    public Result calculateResult(Expression[] expressions, ExpressionContext expressionContext) {
        List<String> separatorLikeFields = getSeparatorLikeFieldNames(expressionContext);
        if (separatorLikeFields.size() > 0) {
            String separatorName = separatorLikeFields.get(0);
            return new TextResult(separatorName);
        } else {
            return new TextResult(DEFAULT_SEPARATOR);
        }
    }

    private List<String> getSeparatorLikeFieldNames(ExpressionContext expressionContext) {
        List<String> results = new ArrayList<String>();
        Project project = expressionContext.getProject();
        int offset = expressionContext.getStartOffset();
        PsiFile file = PsiDocumentManager.getInstance(project).getPsiFile(expressionContext.getEditor().getDocument());
        PsiElement place = file.findElementAt(offset);
        PsiVariable[] variables = MacroUtil.getVariablesVisibleAt(place, "");
        for (PsiVariable variable : variables) {
            if (variable instanceof PsiField && variable.hasModifierProperty("static")) {
                PsiField field = (PsiField) variable;
                String name = field.getName();
                if (FIELD_NAME_SEPARATOR.equals(name)
                        || FIELD_NAME_DELIMITER.equals(name)
                        || FIELD_NAME_DIVIDER.equals(name)) {
                    results.add(name);
                }
            }
        }
        return results;
    }
}

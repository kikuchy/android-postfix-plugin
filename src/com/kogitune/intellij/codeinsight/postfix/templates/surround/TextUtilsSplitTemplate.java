package com.kogitune.intellij.codeinsight.postfix.templates.surround;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.ConstantNode;
import com.intellij.codeInsight.template.impl.MacroCallNode;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.kogitune.intellij.codeinsight.postfix.internal.RichChooserStringBasedPostfixTemplate;
import com.kogitune.intellij.codeinsight.postfix.macro.SeparatorMacro;
import com.kogitune.intellij.codeinsight.postfix.macro.ToStringIfNeedMacro;
import com.kogitune.intellij.codeinsight.postfix.utils.AndroidPostfixTemplatesUtils;
import org.jetbrains.annotations.NotNull;

import static com.intellij.codeInsight.template.postfix.util.JavaPostfixTemplatesUtils.IS_NON_VOID;
import static com.kogitune.intellij.codeinsight.postfix.utils.AndroidClassName.TEXT_UTILS;

/**
 * Postfix template for android TextUtils class.
 *
 * @author kikuchy
 */
public class TextUtilsSplitTemplate extends RichChooserStringBasedPostfixTemplate {

    public static final Condition<PsiElement> IS_NON_NULL = new Condition<PsiElement>() {
        @Override
        public boolean value(PsiElement element) {
            return IS_NON_VOID.value(element) && !AndroidPostfixTemplatesUtils.isAnnotatedNullable(element);
        }

    };

    public TextUtilsSplitTemplate() {
        this("splt");
    }

    public TextUtilsSplitTemplate(@NotNull String alias) {
        super(alias, "TextUtils.split(expr, regexp)", IS_NON_NULL);
    }

    @Override
    public String getTemplateString(@NotNull PsiElement element) {
        return getStaticMethodPrefix(TEXT_UTILS, "split", element) + "($expr$, $regexp$)$END$";
    }

    @Override
    protected void addExprVariable(@NotNull PsiElement expr, Template template) {
        final ToStringIfNeedMacro toStringIfNeedMacro = new ToStringIfNeedMacro();
        MacroCallNode macroCallNode = new MacroCallNode(toStringIfNeedMacro);
        macroCallNode.addParameter(new ConstantNode(expr.getText()));
        template.addVariable("expr", macroCallNode, false);
    }

    @Override
    protected void setVariables(@NotNull Template template, @NotNull PsiElement element) {
        MacroCallNode node = new MacroCallNode(new SeparatorMacro());
        template.addVariable("regexp", node, new ConstantNode(""), false);
    }
}

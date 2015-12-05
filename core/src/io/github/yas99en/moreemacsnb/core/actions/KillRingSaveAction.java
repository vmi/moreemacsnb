/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.text.Caret;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.*;
import org.netbeans.editor.BaseDocument;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;


/**
 *
 * @author yendoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-KillRingSaveAction")
public class KillRingSaveAction extends MoreEmacsAction  {
    public KillRingSaveAction() {
        super("kill-ring-save");
    }

    @Override
    public void actionPerformed(final ActionEvent e, final JTextComponent target) {
        System.out.println("KillRingSaveAction.actionPerformed()");
        final Caret caret = target.getCaret();
        ActionMap actionMap = target.getActionMap();
        if(actionMap == null)  {
            return;
        }
        final Action copyAction = actionMap.get(DefaultEditorKit.copyAction);
        
        if(caret.getDot() != caret.getMark()) {
            copyAction.actionPerformed(e);
            return;
        }
        
        final BaseDocument doc = (BaseDocument)target.getDocument();
        doc.runAtomicAsUser (new Runnable () {
            @Override
            public void run () {
                try {
                    DocumentUtilities.setTypingModification(doc, true);
                    int mark = Mark.get(target);
                    int dot = caret.getDot();
                    caret.setDot(mark);
                    caret.moveDot(dot);
                    target.copy();
                    target.select(dot, dot);
                } finally {
                    DocumentUtilities.setTypingModification(doc, false);
                }
            }         
        });
    }
}

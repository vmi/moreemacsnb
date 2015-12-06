/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.yas99en.moreemacsnb.core.actions;

import io.github.yas99en.moreemacsnb.core.utils.CodePointIterator;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.editor.EditorActionRegistration;

/**
 *
 * @author Yasuhiro Endoh
 */
@EditorActionRegistration(name="io-github-yas99en-moreemacsnb-core-actions-ForwardWordAction")
public class ForwardWordAction extends MoreEmacsAction {
    public ForwardWordAction() {
        super("forward-word");
    }

    @Override
    public void actionPerformed(ActionEvent e, JTextComponent target) {
        Caret caret = target.getCaret();
        int next = getNextWordPosition(target.getDocument(), caret.getDot());
        caret.setDot(next);
    }
    
    public static int getNextWordPosition(Document doc, int offset) {
        String seq;
        try {
            seq = doc.getText(offset, doc.getLength()-offset);
        } catch (BadLocationException ex) {
            Logger.getLogger(ForwardWordAction.class.getName()).log(Level.SEVERE, null, ex);
            throw new AssertionError(ex.getMessage(), ex);
        }
        CodePointIterator itr = new CodePointIterator(seq);
        
        
        for(; itr.hasNext(); ) {
            int codePoint = itr.next();
            if (Character.isLetterOrDigit(codePoint)) {
                itr.previous();
                break;
            }
        }
        for(; itr.hasNext(); ) {
            if (!Character.isLetterOrDigit(itr.next())) {
                itr.previous();
                break;
            }
        }
        
        return offset + itr.index();
    }
}
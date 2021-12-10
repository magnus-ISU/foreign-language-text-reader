/*
 *
 * Foreign Language Text Reader (FLTR) - A Tool for Language Learning.
 *
 * Copyright © 2012-2021 FLTR Developers et al.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package fltrpackage;

import java.awt.Color;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DefaultEditorKit;

public class MultiLineTextField {

	private JTextArea textArea;
	private JScrollPane textAreaScrollPane;

	public MultiLineTextField(String text, int maxChar, int lines, int width, JFrame frame, boolean editable) {
		textArea = new JTextArea(text);
		textArea.setRows(lines);
		textArea.setColumns(width);
		textArea.setFont(UIManager.getDefaults().getFont("JTextField.font"));
		textArea.setEditable(editable);
		if (!editable)
			textArea.setBackground(new Color(230, 230, 230));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		AbstractDocument doc = (AbstractDocument) textArea.getDocument();
		if (maxChar > 0)
			doc.setDocumentFilter(new TextFieldCharLimiter(maxChar));
		MultiLineTextFieldListener listener = new MultiLineTextFieldListener(frame);
		doc.addDocumentListener(listener);
		textArea.addKeyListener(listener);
		textArea.addMouseListener(listener);
		JPopupMenu popupMenu = new JPopupMenu();
		if (editable) {
			Action paste = new DefaultEditorKit.PasteAction();
			paste.putValue(Action.NAME, "Paste");
			popupMenu.add(paste);
		}
		Action copy = new DefaultEditorKit.CopyAction();
		copy.putValue(Action.NAME, "Copy");
		popupMenu.add(copy);
		if (editable) {
			Action cut = new DefaultEditorKit.CutAction();
			cut.putValue(Action.NAME, "Cut");
			popupMenu.add(cut);
		}
		textArea.setComponentPopupMenu(popupMenu);
		textAreaScrollPane = new JScrollPane(textArea);
		textAreaScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public JScrollPane getTextAreaScrollPane() {
		return textAreaScrollPane;
	}

}

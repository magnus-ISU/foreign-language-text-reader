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

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MultiLineTextFieldListener implements KeyListener, DocumentListener, MouseListener {

	private JFrame frame;

	public MultiLineTextFieldListener(JFrame frame) {
		super();
		this.frame = frame;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		if (frame instanceof TermFrame)
			((TermFrame) frame).changesDetected();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		if (frame instanceof TermFrame)
			((TermFrame) frame).changesDetected();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// Tab + ShiftTab to next/prev. field, Enter = default key of frame or
		// disabled
		if ((e.getKeyCode() == KeyEvent.VK_TAB) && (!e.isShiftDown())) {
			e.consume();
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
		} else if ((e.getKeyCode() == KeyEvent.VK_TAB) && e.isShiftDown()) {
			e.consume();
			KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent();
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			JRootPane rootPane = frame.getRootPane();
			JButton dftBtn = rootPane.getDefaultButton();
			if (dftBtn != null) {
				if (dftBtn.isEnabled()) {
					dftBtn.doClick();
				} else {
					e.consume();
				}
			} else {
				e.consume();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (frame instanceof ResultFrame) {
			if (SwingUtilities.isMiddleMouseButton(arg0)) {
				String text = ((ResultFrame) frame).getTfResults().getTextArea().getSelectedText().trim();
				if (!text.equals("")) {
					TermFrame termFrame = FLTR.getTermFrame();
					if (termFrame != null) {
						if (termFrame.isVisible()) {
							String currTrans = termFrame.getTfTranslation().getTextArea().getText();
							if (currTrans.equals("")) {
								termFrame.getTfTranslation().getTextArea().setText(text);
							} else {
								termFrame.getTfTranslation().getTextArea().setText(currTrans + ", " + text);
							}
							termFrame.getButSave().doClick();
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		if (frame instanceof TermFrame)
			((TermFrame) frame).changesDetected();
	}

}

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

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JViewport;

public class TextFrameListener
		implements WindowListener, MouseListener, MouseMotionListener, HierarchyBoundsListener, ActionListener {

	private TextFrame frame;
	private boolean dragging;

	public TextFrameListener(TextFrame frame) {
		super();
		this.frame = frame;
		dragging = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o instanceof JMenuItem) {
			Terms terms = FLTR.getTerms();
			JMenuItem mi = (JMenuItem) o;
			TermFrame termFrame = FLTR.getTermFrame();
			if (termFrame == null) {
				termFrame = new TermFrame();
				FLTR.setTermFrame(termFrame);
			}
			if (((String) mi.getClientProperty("action")).equals("edit")) {
				termFrame.startEdit((Term) mi.getClientProperty("term"), "");
			} else if (((String) mi.getClientProperty("action")).equals("browser")) {
				File textFile = Utilities.CreateTempFile("Text_", ".htm", FLTR.getLanguage().getTextDir());
				if (!FLTR.getText().saveTextToHTMLFileForReview(textFile)) {
					Utilities.showErrorMessage("No Text to display.");
				} else {
					Utilities.openURLInDefaultBrowser(textFile.toURI().toString());
				}
			} else if (((String) mi.getClientProperty("action")).equals("info")) {
				Utilities.showInfoMessage(FLTR.getText().getInfo() + "-----------------------\n" + terms.getInfo());
			} else if (((String) mi.getClientProperty("action")).equals("saveterms")) {
				if (terms.isDirty()) {
					String backupFilename = terms.getFile().getAbsolutePath();
					backupFilename = backupFilename.substring(0,
							backupFilename.length() - Constants.TEXT_FILE_EXTENSION_LENGTH) + ".bak";
					if (Utilities.renameFile(terms.getFile(), (new File(backupFilename))) == false) {
						Utilities.showErrorMessage(
								"Renaming your Vocabulary for Backup has failed.\n\nSorry, saving your vocabulary seems not be possible.");
					} else {
						if (terms.isSaveTermsToFileOK() == false) {
							Utilities
									.showErrorMessage("Writing your Vocabulary to\n" + terms.getFile().getAbsolutePath()
											+ "\nhas failed.\n\nSorry, saving your vocabulary seems not be possible.");
						} else {
							Utilities.showInfoMessage("Success!\n\nYour Vocabulary has been successfully written to\n"
									+ terms.getFile().getAbsolutePath() + "\n\nThe previous version is available in\n"
									+ backupFilename);
						}
					}
				} else {
					Utilities.showInfoMessage("There is no need to save your vocabulary as nothing has changed.");
				}
			} else if (((String) mi.getClientProperty("action")).equals("exportterms")) {
				File f = terms.getExportFile();
				boolean exportOK = terms.isExportTermsToFileOK();
				if (!exportOK) {
					if (f != null) {
						if (f.exists() && f.isFile()) {
							f.delete();
						}
					}
				} else {
					exportOK = (f.exists() && f.isFile());
				}
				if (!exportOK) {
					Utilities.showErrorMessage("Exporting your Vocabulary to\n" + f.getAbsolutePath()
							+ "\nhas failed,\nor is deactivated (DoExport=0),\nor the Export Template is empty,"
							+ "\nor the Export Status List is empty.\n\nSorry, Exporting seems not be possible.");
				} else {
					Utilities.showInfoMessage(
							"Success!\n\nYour Vocabulary has been exported to\n" + f.getAbsolutePath());
				}
			} else if (((String) mi.getClientProperty("action")).equals("miss")) {
				int missSent = FLTR.getText().setMissingSentences();
				Utilities.showInfoMessage(String.valueOf(missSent) + " Sentences in existent Terms added.");
			} else if (((String) mi.getClientProperty("action")).equals("delete")) {
				String termtext = ((Term) mi.getClientProperty("term")).getTerm();
				terms.removeTerm((Term) mi.getClientProperty("term"));
				if (FLTR.getTermFrame().isVisible()
						&& FLTR.getTermFrame().getTfTerm().getTextArea().getText().equals(termtext))
					FLTR.getTermFrame().setVisible(false);
				FLTR.getText().matchWithTerms();
				frame.getTextPanel().repaint();
				frame.getTextPanel().requestFocus();
			} else if (((String) mi.getClientProperty("action")).equals("new")) {
				Text t = FLTR.getText();
				TextItem ti = (TextItem) mi.getClientProperty("textitem");
				String s = ti.getTextItemValue();
				int index = t.getTextItems().indexOf(ti);
				t.setMarkIndexStart(index);
				t.setMarkIndexEnd(index);
				t.setRangeMarked(true);
				termFrame.startNew(s, t.getMarkedTextSentence(s));
			} else if (((String) mi.getClientProperty("action")).equals("dict")) {
				String word = (String) mi.getClientProperty("word");
				int link = ((Integer) mi.getClientProperty("link")).intValue();
				FLTR.getLanguage().lookupWordInBrowser(word, link, true);
			} else if (((String) mi.getClientProperty("action")).equals("setstatus")) {
				Term t = (Term) mi.getClientProperty("term");
				String termtext = t.getTerm();
				TermStatus ts = (TermStatus) mi.getClientProperty("status");
				t.setStatus(ts);
				terms.setDirty(true);
				if (t.getTranslation().isEmpty() && (ts.compareTo(TermStatus.Known) <= 0)) {
					Utilities.showInfoMessage(
							"Translation must not be empty,\nunless status is 'Ignored' or 'Well Known'.\nPlease correct this…");
					termFrame.startEdit((Term) mi.getClientProperty("term"), "");
				}
				frame.getTextPanel().repaint();
				frame.getTextPanel().requestFocus();
				if (FLTR.getTermFrame().isVisible()
						&& FLTR.getTermFrame().getTfTerm().getTextArea().getText().equals(termtext))
					FLTR.getTermFrame().setRbStatus(ts);
			} else if (((String) mi.getClientProperty("action")).equals("knowthis")) {
				TermStatus ts = (TermStatus) mi.getClientProperty("status");
				Text t = FLTR.getText();
				TextItem ti = (TextItem) mi.getClientProperty("textitem");
				String s = ti.getTextItemValue();
				int index = t.getTextItems().indexOf(ti);
				t.setMarkIndexStart(index);
				t.setMarkIndexEnd(index);
				t.setRangeMarked(true);
				terms.addTerm(new Term(s, "", t.getMarkedTextSentence(s).replace(Constants.PARAGRAPH_MARKER, ""), "",
						ts.getStatusCode()));
				FLTR.getText().matchWithTerms();
				frame.getTextPanel().repaint();
				frame.getTextPanel().requestFocus();
			} else if (((String) mi.getClientProperty("action")).equals("knowall")) {
				if (Utilities.showYesNoQuestion("I KNOW ALL! - Really? Are you sure?", false)) {
					TermStatus ts = (TermStatus) mi.getClientProperty("status");
					Text t = FLTR.getText();
					for (TextItem ti : t.getTextItems()) {
						if (ti.getLink() == null) {
							String s = ti.getTextItemValue().replace(Constants.PARAGRAPH_MARKER, "");
							if ((!s.isEmpty()) && (terms.getTermFromKey(s.toLowerCase()) == null)) {
								int index = t.getTextItems().indexOf(ti);
								t.setMarkIndexStart(index);
								t.setMarkIndexEnd(index);
								t.setRangeMarked(true);
								terms.addTerm(new Term(s, "",
										t.getMarkedTextSentence(s).replace(Constants.PARAGRAPH_MARKER, ""), "",
										ts.getStatusCode()));
							}
						}
					}
					FLTR.getText().matchWithTerms();
					frame.getTextPanel().repaint();
					frame.getTextPanel().requestFocus();
				}
			}
		}
	}

	@Override
	public void ancestorMoved(HierarchyEvent e) {
		frame.getPopupMenu().setVisible(false);
		Point p = frame.getLocation();
		Preferences.putCurrXPosTextWindow(p.x);
		Preferences.putCurrYPosTextWindow(p.y);
	}

	@Override
	public void ancestorResized(HierarchyEvent e) {
		frame.getPopupMenu().setVisible(false);
	}

	private boolean isPopupTriggerHandled(MouseEvent e) {
		boolean r;
		if (e.isPopupTrigger()) {
			TextPanelPopupMenu popupMenu = frame.getPopupMenu();
			Text text = FLTR.getText();
			Point p1 = e.getPoint();
			Point p2 = ((JViewport) e.getSource()).getViewPosition();
			Point p3 = new Point(p1.x + p2.x, p1.y + p2.y);
			int index = text.getPointedTextItemIndex(p3);
			TextItem ti = null;
			String w2 = "";
			String w3 = "";
			if (index >= 0) {
				ti = text.getTextItems().get(index);
				if ((index + 1) < text.getTextItems().size()) {
					w2 = ti.getTextItemValue() + ti.getAfterItemValue()
							+ text.getTextItems().get(index + 1).getTextItemValue();
					if ((index + 2) < text.getTextItems().size()) {
						w3 = w2 + text.getTextItems().get(index + 1).getAfterItemValue()
								+ text.getTextItems().get(index + 2).getTextItemValue();
					}
				}
			}
			popupMenu.updateMenu(ti, w2, w3);
			popupMenu.show((Component) e.getSource(), e.getX() + 10, e.getY() + 10);
			r = true;
		} else {
			r = false;
		}
		return r;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			Text text = FLTR.getText();
			Point p1 = e.getPoint();
			Point p2 = ((JViewport) e.getSource()).getViewPosition();
			Point p3 = new Point(p1.x + p2.x, p1.y + p2.y);
			int endIndex = FLTR.getText().getPointedTextItemIndex(p3);
			if ((!text.isRangeMarked()) && (endIndex >= 0)) {
				text.setRangeMarked(true);
			}
			if (endIndex >= 0) {
				text.setMarkIndexEnd(endIndex);
			}
			frame.getTextPanel().repaint();
			frame.getTextPanel().requestFocus();
			dragging = true;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		Text text = FLTR.getText();
		Point p1 = e.getPoint();
		Point p2 = ((JViewport) e.getSource()).getViewPosition();
		Point p3 = new Point(p1.x + p2.x, p1.y + p2.y);
		int index = text.getPointedTextItemIndex(p3);
		if (index >= 0) {
			Term t = text.getTextItems().get(index).getLink();
			if (t == null) {
				String s = text.getTextItems().get(index).getTextItemValue().trim();
				if (!s.equals("")) {
					frame.getLabinfo()
							.setText("<html><div style=\"text-align:"
									+ (FLTR.getLanguage().getRightToLeft() ? "right" : "left") + ";width:"
									+ String.valueOf(Preferences.getCurrWidthTextPanel()) + ";\">"
									+ Utilities.escapeHTML(s) + "<br>" + "(New Word)</div></html>");
					Utilities.setComponentOrientation(frame.getLabinfo());
					Utilities.setHorizontalAlignment(frame.getLabinfo());
					frame.pack();
				}
			} else {
				frame.getLabinfo()
						.setText("<html><div style=\"text-align:"
								+ (FLTR.getLanguage().getRightToLeft() ? "right" : "left") + ";width:"
								+ String.valueOf(Preferences.getCurrWidthTextPanel()) + ";\">"
								+ t.displayWithStatusHTML() + "</div></html>");
				Utilities.setComponentOrientation(frame.getLabinfo());
				Utilities.setHorizontalAlignment(frame.getLabinfo());
				frame.pack();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isPopupTriggerHandled(e)) {
			return;
		}
		if (e.getButton() == MouseEvent.BUTTON1) {
			Text text = FLTR.getText();
			Point p1 = e.getPoint();
			Point p2 = ((JViewport) e.getSource()).getViewPosition();
			Point p3 = new Point(p1.x + p2.x, p1.y + p2.y);
			int startIndex = text.getPointedTextItemIndex(p3);
			text.setRangeMarked(startIndex >= 0);
			text.setMarkIndexStart(startIndex);
			dragging = false;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isPopupTriggerHandled(e)) {
			return;
		}
		if (e.getButton() == MouseEvent.BUTTON1) {
			Text text = FLTR.getText();
			Point p1 = e.getPoint();
			Point p2 = ((JViewport) e.getSource()).getViewPosition();
			Point p3 = new Point(p1.x + p2.x, p1.y + p2.y);
			int endIndex = text.getPointedTextItemIndex(p3);
			if ((!text.isRangeMarked()) && (endIndex >= 0)) {
				text.setRangeMarked(true);
			}
			if (endIndex >= 0) {
				text.setMarkIndexEnd(endIndex);
			}
			frame.getTextPanel().repaint();
			frame.getTextPanel().requestFocus();
			String s = text.getMarkedText(dragging).replace(Constants.PARAGRAPH_MARKER, "");
			if (!s.equals("")) {
				Term t = FLTR.getTerms().getTermFromKey(s.toLowerCase());
				TermFrame termFrame = FLTR.getTermFrame();
				if (termFrame == null) {
					termFrame = new TermFrame();
					FLTR.setTermFrame(termFrame);
				}
				if (t == null) {
					termFrame.startNew(s, text.getMarkedTextSentence(s));
				} else {
					termFrame.startEdit(t, text.getMarkedTextSentence(s));
				}
				Language lang = FLTR.getLanguage();
				lang.lookupWordInBrowser(s, 3, false);
				lang.lookupWordInBrowser(s, 2, false);
				lang.lookupWordInBrowser(s, 1, false);
			}
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		Terms terms = FLTR.getTerms();
		File f = terms.getExportFile();
		boolean exportOK = terms.isExportTermsToFileOK();
		if (!exportOK) {
			if (f != null) {
				if (f.exists() && f.isFile()) {
					f.delete();
				}
			}
		} else {
			exportOK = (f.exists() && f.isFile());
		}
		if (terms.isDirty()) {
			if (Utilities.showYesNoQuestion(
					"Your Vocabulary\n" + terms.getFile().getAbsolutePath() + "\nhas changed.\n\nSave to disk?",
					true)) {
				String backupFilename = terms.getFile().getAbsolutePath();
				backupFilename = backupFilename.substring(0,
						backupFilename.length() - Constants.TEXT_FILE_EXTENSION_LENGTH) + ".bak";
				if (Utilities.renameFile(terms.getFile(), (new File(backupFilename))) == false) {
					Utilities.showErrorMessage(
							"Renaming your Vocabulary for Backup has failed.\n\nSorry, saving your vocabulary seems not be possible.");
				} else {
					if (terms.isSaveTermsToFileOK() == false) {
						Utilities.showErrorMessage("Writing your Vocabulary to\n" + terms.getFile().getAbsolutePath()
								+ "\nhas failed.\n\nSorry, saving your vocabulary seems not be possible.");
					} else {
						if (!exportOK) {
							Utilities.showInfoMessage("Success!\n\nYour Vocabulary has been successfully written to\n"
									+ terms.getFile().getAbsolutePath() + "\n\nThe previous version is available in\n"
									+ backupFilename);
						} else {
							Utilities.showInfoMessage("Success!\n\nYour Vocabulary has been successfully written to\n"
									+ terms.getFile().getAbsolutePath() + "\n\nThe previous version is available in\n"
									+ backupFilename + "\n\nThe Vocabulary has been also exported to\n"
									+ f.getAbsolutePath());
						}
					}
				}
			}
		}

		Preferences.putCurrTextScrollPosition(frame.getTextPanel().getViewPort().getViewPosition().y);

		TermFrame termFrame = FLTR.getTermFrame();
		if (termFrame != null) {
			termFrame.setVisible(false);
			termFrame.dispose();
			FLTR.setTermFrame(null);
		}
		ResultFrame resultFrame = FLTR.getResultFrame();
		if (resultFrame != null) {
			resultFrame.setVisible(false);
			resultFrame.dispose();
			FLTR.setResultFrame(null);
		}

		frame.setVisible(false);
		frame.dispose();
		FLTR.setTextFrame(null);

		terms = null;
		FLTR.setTerms(terms);

		Text text = FLTR.getText();
		text = null;
		FLTR.setText(text);

		FLTR.getStartFrame().setVisible(true);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

}

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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

import fltrpackage.Constants.OS_T;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class TermFrame extends JFrame {

	private MultiLineTextField tfTerm;
	private MultiLineTextField tfTranslation;
	private MultiLineTextField tfRomanization;
	private MultiLineTextField tfSentence;
	private MultiLineTextField tfSimilar;
	private JRadioButton[] rbStatus;
	private JButton butDelete;
	private JButton butSave;
	private JButton butLookup1;
	private JButton butLookup2;
	private JButton butLookup3;
	private TermFrameListener listener;
	private String originalKey;
	private int maxSim;

	public TermFrame() {
		super();
		setTitle("Term");
		listener = new TermFrameListener(this);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		originalKey = "";

		JPanel mainPanel = new JPanel();
		MigLayout mainPanelLayout = new MigLayout("fill", // Layout Constraints
				"[right]rel[]", // Column constraints
				"[]3[]"); // Row constraints
		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(new JLabel("Term:"), "right");
		tfTerm = new MultiLineTextField("", 200, 2, 35, this, false);
		mainPanel.add(tfTerm.getTextAreaScrollPane(), "wrap");

		maxSim = Preferences.getCurrMaxSimTerms();
		if (maxSim > 0) {
			mainPanel.add(new JLabel("Similar Terms:"), "right");
			tfSimilar = new MultiLineTextField("", maxSim * 1000, Integer.min(3, maxSim), 35, this, false);
			mainPanel.add(tfSimilar.getTextAreaScrollPane(), "wrap");
		}

		mainPanel.add(new JLabel("Translation:"), "right");
		tfTranslation = new MultiLineTextField("", 200, 2, 35, this, true);
		mainPanel.add(tfTranslation.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Romanization:"), "right");
		tfRomanization = new MultiLineTextField("", 200, 2, 35, this, true);
		mainPanel.add(tfRomanization.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Sentence:"), "right");
		tfSentence = new MultiLineTextField("", 400, 2, 35, this, true);
		mainPanel.add(tfSentence.getTextAreaScrollPane(), "wrap");

		mainPanel.add(new JLabel("Status:"), "gapbottom 10px, right");
		rbStatus = new JRadioButton[TermStatus.values().length - 1];
		ButtonGroup bgStatus = new ButtonGroup();
		for (int i = 0; i < rbStatus.length; i++) {
			rbStatus[i] = new JRadioButton(String.valueOf(i + 1));
			rbStatus[i].addActionListener(listener);
			bgStatus.add(rbStatus[i]);
			mainPanel.add(rbStatus[i],
					"gapbottom 10px" + (i == 0 ? ", split" : (i == (rbStatus.length - 1) ? ", wrap" : "")));
		}
		rbStatus[5].setText("Ign");
		rbStatus[6].setText("WKn");

		butDelete = new JButton("Delete");
		butDelete.addActionListener(listener);
		mainPanel.add(butDelete, "left");

		Language lang = FLTR.getLanguage();

		butLookup1 = new JButton("Dict1");
		butLookup1.setEnabled(lang.isURLset(1) || lang.isShellCommandSet(1));
		butLookup1.addActionListener(listener);
		mainPanel.add(butLookup1, "split 4, right");

		butLookup2 = new JButton("Dict2");
		butLookup2.setEnabled(lang.isURLset(2) || lang.isShellCommandSet(2));
		butLookup2.addActionListener(listener);
		mainPanel.add(butLookup2, "center");

		butLookup3 = new JButton("Dict3");
		butLookup3.setEnabled(lang.isURLset(3) || lang.isShellCommandSet(3));
		butLookup3.addActionListener(listener);
		mainPanel.add(butLookup3, "left");

		butSave = new JButton("Save");
		butSave.addActionListener(listener);
		mainPanel.add(butSave, "gapleft 30px, grow, right");

		getContentPane().add(mainPanel);

		JRootPane rootPane = getRootPane();
		rootPane.setDefaultButton(butSave);

		pack();
		setResizable(false);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(Preferences.getCurrXPosTermWindow((d.width - this.getSize().width) / 2),
				Preferences.getCurrYPosTermWindow((d.height - this.getSize().height) / 2));
		getContentPane().addHierarchyBoundsListener(listener);

//		if (Constants.OS != OS_T.MACOS) {
//			setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(Constants.ICONPATH)));
//		}

	}

	public void changesDetected() {
		String term = Utilities.replaceControlCharactersWithSpace(getTfTerm().getTextArea().getText());
		String translation = Utilities.replaceControlCharactersWithSpace(getTfTranslation().getTextArea().getText());
		String romanization = Utilities.replaceControlCharactersWithSpace(getTfRomanization().getTextArea().getText());
		String sentence = Utilities.replaceControlCharactersWithSpace(getTfSentence().getTextArea().getText());
		TermStatus status = getRbStatus();
		String key = term.toLowerCase();
		Terms terms = FLTR.getTerms();
		boolean exists = (terms.getTermFromKey(key) != null);
		if (!exists) {
			butSave.setText("NEW - Save");
			butSave.setEnabled(true);
			if (!(status.equals(TermStatus.Ignored) || status.equals(TermStatus.WellKnown))) {
				if (translation.equals("")) {
					butSave.setEnabled(false);
				}
			}
		} else {
			String oldtermstring = terms.getTermFromKey(key).toString();
			String newtermstring = (new Term(term, translation, sentence, romanization, status.getStatusCode()))
					.toString();
			if (oldtermstring.equals(newtermstring)) {
				butSave.setText("Save");
				butSave.setEnabled(false);
			} else {
				butSave.setText("CHANGED - Save");
				butSave.setEnabled(true);
			}
		}
	}

	public void fillSimTerms(String termstring, boolean exists) {
		String s = "";
		int i = 0;
		ArrayList<Term> list = FLTR.getTerms().getNearlyEquals(termstring, (exists ? (1 + maxSim) : maxSim));
		for (Term tt : list) {
			if (!(exists && tt.getTerm().equalsIgnoreCase(termstring))) {
				i++;
				s += Integer.toString(i) + ") " + tt.displayWithoutStatus() + "\n";
			}
		}
		if (i == 0) {
			s = "[None]\n";
		}
		if (i == 1) {
			s = s.substring(3, s.length());
		}
		tfSimilar.getTextArea().setText(s.substring(0, s.length() - 1));
		tfSimilar.getTextArea().setCaretPosition(0);
	}

	public JButton getButDelete() {
		return butDelete;
	}

	public JButton getButLookup1() {
		return butLookup1;
	}

	public JButton getButLookup2() {
		return butLookup2;
	}

	public JButton getButLookup3() {
		return butLookup3;
	}

	public JButton getButSave() {
		return butSave;
	}

	public String getOriginalKey() {
		return originalKey;
	}

	public TermStatus getRbStatus() {
		for (int i = 0; i < rbStatus.length; i++) {
			if (rbStatus[i].isSelected()) {
				return TermStatus.getStatusFromOrdinal(i + 1);
			}
		}
		return TermStatus.Unknown;
	}

	public MultiLineTextField getTfRomanization() {
		return tfRomanization;
	}

	public MultiLineTextField getTfSentence() {
		return tfSentence;
	}

	public MultiLineTextField getTfTerm() {
		return tfTerm;
	}

	public MultiLineTextField getTfTranslation() {
		return tfTranslation;
	}

	public void setRbStatus(TermStatus ts) {
		int index = ts.ordinal() - 1;
		int count = 0;
		for (int i = 0; i < rbStatus.length; i++) {
			rbStatus[i].setSelected(i == index);
			if (i == index) {
				count++;
			}
		}
		if (count == 0) {
			rbStatus[0].setSelected(true);
		}
	}

	public void startEdit(Term t, String sentence) {
		Utilities.setComponentOrientation(tfTerm.getTextArea());
		Utilities.setComponentOrientation(tfSentence.getTextArea());
		if (maxSim > 0)
			Utilities.setComponentOrientation(tfSimilar.getTextArea());
		setTitle("Edit Term");
		originalKey = t.getKey();
		tfTerm.getTextArea().setText(t.getTerm());
		tfTranslation.getTextArea().setText(t.getTranslation());
		tfRomanization.getTextArea().setText(t.getRomanization());
		if (t.getSentence().trim().equals("")) {
			if ((!Preferences.getCurrText().equals(Constants.VOCAB_FILE_NAME)) && (!sentence.equals(""))) {
				tfSentence.getTextArea().setText(sentence);
			} else {
				tfSentence.getTextArea().setText("");
			}
		} else {
			tfSentence.getTextArea().setText(t.getSentence());
		}
		setRbStatus(t.getStatus());
		if (maxSim > 0)
			fillSimTerms(t.getTerm(), true);
		changesDetected();
		pack();
		setVisible(true);
		tfTranslation.getTextArea().requestFocusInWindow();
	}

	public void startNew(String term, String sentence) {
		Utilities.setComponentOrientation(tfTerm.getTextArea());
		Utilities.setComponentOrientation(tfSentence.getTextArea());
		if (maxSim > 0)
			Utilities.setComponentOrientation(tfSimilar.getTextArea());
		setTitle("New Term");
		originalKey = term.toLowerCase();
		tfTerm.getTextArea().setText(term);
		tfTranslation.getTextArea().setText("");
		tfRomanization.getTextArea().setText("");
		if (!Preferences.getCurrText().equals(Constants.VOCAB_FILE_NAME)) {
			tfSentence.getTextArea().setText(sentence);
		} else {
			tfSentence.getTextArea().setText("");
		}
		setRbStatus(TermStatus.Unknown);
		if (maxSim > 0)
			fillSimTerms(term, false);
		changesDetected();
		pack();
		setVisible(true);
		tfTranslation.getTextArea().requestFocusInWindow();
	}

}

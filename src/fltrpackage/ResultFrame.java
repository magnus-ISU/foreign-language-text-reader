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
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import fltrpackage.Constants.OS_T;

@SuppressWarnings("serial")
public class ResultFrame extends JFrame {

	private MultiLineTextField tfResults;
	private JButton butKill;
	private ResultFrameListener listener;
	private Process process;

	public ResultFrame() {
		super();
		setTitle("Dictionary Query Results");
		listener = new ResultFrameListener(this);
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		tfResults = new MultiLineTextField("", -1, Preferences.getCurrResultLines(), 35, this, false);
		mainPanel.add(tfResults.getTextAreaScrollPane());

		JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		butKill = new JButton("KILL");
		setProcess(null);
		butKill.setEnabled(false);
		butKill.addActionListener(listener);
		JLabel lbInfo = new JLabel("> Mark & Mouse Middle Click to transfer to Term Window as translation");
		subPanel.add(butKill);
		subPanel.add(lbInfo);
		subPanel.setBorder(new EmptyBorder(0, -5, 0, 0));
		mainPanel.add(subPanel);

		getContentPane().add(mainPanel);

		pack();
		setResizable(false);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(Preferences.getCurrXPosResultWindow((d.width - this.getSize().width) / 2),
				Preferences.getCurrYPosResultWindow((d.height - this.getSize().height) / 2));
		getContentPane().addHierarchyBoundsListener(listener);

		if (Constants.OS != OS_T.MACOS) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getResource(Constants.ICONPATH)));
		}

	}

	public JButton getButKill() {
		return butKill;
	}

	public Process getProcess() {
		return process;
	}

	public MultiLineTextField getTfResults() {
		return tfResults;
	}

	public void putTextIntoTfResults(String s) {
		getTfResults().getTextArea().setText(s);
		getTfResults().getTextArea().setCaretPosition(0);
	}

	public void setProcess(Process process) {
		this.process = process;
	}

}

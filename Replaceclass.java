import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Replaceclass extends JDialog {

    private JTextField search;
    private JTextField replace;
    private JLabel searchfor;
    private JLabel replacewith;
    private JButton bclose;
    private JButton breplaceall;
    private boolean breplace;
    private JButton bfind;
    private JTextArea log;
    public Replaceclass(JTextArea log, Frame parent) {
        super(parent, "Replace", true);
	this.log=log;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
        searchfor = new JLabel("Search For : ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        panel.add(searchfor, cs);

        search = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        panel.add(search, cs);

        replacewith = new JLabel("Replace With : ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panel.add(replacewith, cs);

        replace = new JTextField(20);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        panel.add(replace, cs);
        panel.setBorder(new LineBorder(Color.GRAY));


        bclose = new JButton("Close");

        bclose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {dispose();}});
       bfind = new JButton("Find");
        bfind.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                search();
            }
        });
	 breplaceall = new JButton("Replace All");

        breplaceall.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {replaceAll();}});
      /* breplace = new JButton("Replace");
        breplace.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                replace();
            }
        });*/
        JPanel bp = new JPanel();
        bp.add(bclose);
	 bp.add(breplaceall);
        bp.add(bfind);



        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
	setVisible(true);
    }

   public String getFind() {
        return search.getText().trim();
    }

    public String getReplace() {
       return replace.getText().trim();
    }

	public void search(){
		String str= getFind();
		if(str!=null){
		String content=log.getText();
		int ind=0;
		int arr[]=new int[40];
		int val=0;
		do{
			if(val==0)
			arr[val]=content.indexOf(str,0);
			else
			arr[val]=content.indexOf(str,arr[val-1]+(str.length()));
			ind++;
			val++;
		}while(arr[val-1]!=-1);
		for(int j=0;j<val-1;j++)
			System.out.println(arr[j]);
		}
	}
	public void replaceAll(){
		String str= getFind();
		String str1= "";
		str1=getReplace();
		if(str!=null){
		String content;log.getText();
		int ind=0;
		int arr[]=new int[40];
		int val=0;
		do{	content=log.getText();
			if(val==0)
			arr[val]=content.indexOf(str,0);
			else
			arr[val]=content.indexOf(str,arr[val-1]+(str1.length()));
			if(arr[val]!=-1)
			log.replaceRange(str1,arr[val],(arr[val]+str.length()));
			ind++;
			val++;
		}while(arr[val-1]!=-1);
		}
	}
}

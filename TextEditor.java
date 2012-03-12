import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import java.awt.datatransfer.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

class TextEditor extends JFrame implements ActionListener{
	JMenuBar menuBar;
	static int tabno;
	JMenu file, edit,search,help;
	JMenuItem newdoc,open,save,saveas,close,quit,undo,redo,cut,copy,paste,find,replace,about;
	JButton bnew,bopen,bsave,bcut,bcopy,bpaste,bfind,breplace;	
	public JButton bundo,bredo;
	JFileChooser fc;
	JTabbedPane jtp;
	ImageIcon icon;
	DocumentPanel panel[];

	public TextEditor(){
		panel=new DocumentPanel[10];
		Container con= getContentPane();
		jtp=new JTabbedPane();
		icon = new ImageIcon("images/empty.png");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(d.width,d.height-30);
		Toolkit tool = Toolkit.getDefaultToolkit();
		Image a = tool.getImage( "images/text-editor.png" );
		setIconImage(a);
		con.setLayout(new BorderLayout());
		panel[0]=new DocumentPanel(null,this);
		jtp.addTab("Untitled Document",icon,panel[0],"Blank Document");
		jtp.setTabComponentAt(0,new ButtonTabComponent(jtp));
		setTitle(jtp.getTitleAt(0)+" - JVEdit");
		tabno=0;
		con.add(jtp);
		jtp.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent evt){
				JTabbedPane jp=(JTabbedPane)evt.getSource();
				int sel=jp.getSelectedIndex();
				if(sel!=-1){
		 		setTitle(jp.getTitleAt(sel)+"(~"+jp.getToolTipTextAt(sel)+") - JVEdit");
				panel[sel].updateButtons();
				}
				else
					setTitle("JVEdit");
			}
		});
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		addMenu();
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(true);
		addButtons(toolBar);
		con.add(toolBar, BorderLayout.NORTH);
		fc = new JFileChooser();	
		addWindowListener(new WindowAdapter()
		{
			 public void windowClosing(WindowEvent we)
			{  String s1 = "Save";
            String s2 = "Close without Saving";
            String s3 = "Cancel";
		Object[] options1 = {s1, s2, s3};
			int result=JOptionPane.showOptionDialog(
                          TextEditor.this, "Save changes to document before closing?", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,options1,s1);
                            if(result==JOptionPane.NO_OPTION)
				System.exit(0);
			else dispose();
			}
		});
	}
	protected void addMenu(){
		file = new JMenu("File");
		file.setMnemonic('F');
		menuBar.add(file);

		newdoc = new JMenuItem("New", KeyEvent.VK_N);
		newdoc.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	        newdoc.addActionListener(this);
	        file.add(newdoc);
		
		open = new JMenuItem("Open...", KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        	open.addActionListener(this);
        	file.add(open);
		
		file.addSeparator();

		save = new JMenuItem("Save", KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	        save.addActionListener(this);
	        file.add(save);

		saveas = new JMenuItem("Save As...", KeyEvent.VK_A);
		saveas.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
		saveas.addActionListener(this);
        	file.add(saveas);

		file.addSeparator();

		close = new JMenuItem("Close", KeyEvent.VK_C);
		close.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_W, ActionEvent.CTRL_MASK));
	        close.addActionListener(this);
	        file.add(close);

		quit = new JMenuItem("Quit");
		quit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        	quit.addActionListener(this);
        	file.add(quit);

		edit = new JMenu("Edit");
		edit.setMnemonic('E');
		menuBar.add(edit);

		undo = new JMenuItem("Undo", KeyEvent.VK_U);
		undo.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
	        undo.addActionListener(this);
	        edit.add(undo);

		redo = new JMenuItem("Redo", KeyEvent.VK_R);
		redo.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, ActionEvent.CTRL_MASK|ActionEvent.SHIFT_MASK));
	        redo.addActionListener(this);
	        edit.add(redo);

		edit.addSeparator();

		cut = new JMenuItem("Cut", KeyEvent.VK_T);
		cut.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, ActionEvent.CTRL_MASK));
	        cut.addActionListener(this);
	        edit.add(cut);

		copy = new JMenuItem("Copy", KeyEvent.VK_C);
		copy.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.CTRL_MASK));
	        copy.addActionListener(this);
	        edit.add(copy);

		paste = new JMenuItem("Paste", KeyEvent.VK_P);
		paste.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_V, ActionEvent.CTRL_MASK));
	        paste.addActionListener(this);
	        edit.add(paste);

		search = new JMenu("Search");
		search.setMnemonic('S');
		menuBar.add(search);

		find = new JMenuItem("Find...", KeyEvent.VK_F);
		find.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F, ActionEvent.CTRL_MASK));
	        find.addActionListener(this);
	        search.add(find);

		replace = new JMenuItem("Replace...", KeyEvent.VK_R);
		replace.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, ActionEvent.CTRL_MASK));
	        replace.addActionListener(this);
	        search.add(replace);

		help = new JMenu("Help");
		help.setMnemonic('H');
		menuBar.add(help);

		about = new JMenuItem("About", KeyEvent.VK_A);
	        about.addActionListener(this);
	        help.add(about);
	}
	protected void addButtons(JToolBar toolBar) {
        	bnew = new JButton(new ImageIcon("images/document-new.png"));
        	bnew.setToolTipText("Create a new Document");
        	bnew.addActionListener(this);
        	toolBar.add(bnew);

        	bopen= new JButton("Open",new ImageIcon("images/document-open.png"));
       	 	bopen.setToolTipText("Open a file");
        	bopen.addActionListener(this);
		toolBar.add(bopen);

		bsave = new JButton("Save",new ImageIcon("images/document-save.png"));
		bsave.setToolTipText("Save the current file");
		bsave.addActionListener(this);
		toolBar.add(bsave);
		toolBar.addSeparator();
	 
		bundo= new JButton("Undo",new ImageIcon("images/edit-undo.png"));
		bundo.setToolTipText("Undo the last action");
		bundo.addActionListener(this);
		toolBar.add(bundo);

		bredo = new JButton(new ImageIcon("images/edit-redo.png"));
		bredo.setToolTipText("Redo the last undone action");
		bredo.addActionListener(this);
		toolBar.add(bredo);
		toolBar.addSeparator();
	       
		bcut = new JButton(new ImageIcon("images/edit-cut.png"));
		bcut.setToolTipText("Cut the selection");
		bcut.addActionListener(this);
		toolBar.add(bcut);

		bcopy= new JButton(new ImageIcon("images/edit-copy.png"));
		bcopy.setToolTipText("Copy the selection");
		bcopy.addActionListener(this);
		toolBar.add(bcopy);

		bpaste = new JButton(new ImageIcon("images/edit-paste.png"));
		bpaste.setToolTipText("Paste the clipboard");
		bpaste.addActionListener(this);
		toolBar.add(bpaste);
		toolBar.addSeparator();
	 
		bfind= new JButton(new ImageIcon("images/edit-find.png"));
		bfind.setToolTipText("Search for text");
		bfind.addActionListener(this);
		toolBar.add(bfind);

		breplace = new JButton(new ImageIcon("images/edit-find-replace.png"));
		breplace.setToolTipText("Search for and replace text");
		breplace.addActionListener(this);
		toolBar.add(breplace);
		
		bundo.setEnabled(false);
   		bredo.setEnabled(false);
	}

	  public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Open...")||e.getSource()==bopen){
			try{
				openDocument();
			}
			catch(IOException excp){
			}	
		}
		else if(e.getActionCommand().equals("Save As...")){
			try{
				File files=panel[jtp.getSelectedIndex()].saveAsDocument();
				jtp.setTitleAt(jtp.getSelectedIndex(),files.getName());	
				jtp.setToolTipTextAt(jtp.getSelectedIndex(),"Name : ~"+files.getPath());	
			}
			catch(IOException excp){
			}
		}
		else if(e.getActionCommand().equals("Save")||e.getSource()==bsave){
			try{
				File files=panel[jtp.getSelectedIndex()].saveDocument();
				jtp.setTitleAt(jtp.getSelectedIndex(),files.getName());					
				jtp.setToolTipTextAt(jtp.getSelectedIndex(),"Name : ~"+files.getPath());
			}
			catch(IOException excp){
			}
		}
		else if(e.getActionCommand().equals("New")||e.getSource()==bnew)
			newDocument();
		else if(e.getActionCommand().equals("Cut")||e.getSource()==bcut)
			panel[jtp.getSelectedIndex()].cutSelection();
		else if(e.getActionCommand().equals("Copy")||e.getSource()==bcopy)
			panel[jtp.getSelectedIndex()].copySelection();
		else if(e.getActionCommand().equals("Paste")||e.getSource()==bpaste){
			try{
				panel[jtp.getSelectedIndex()].pasteClipboard();
			}
			catch(Exception excp){
			}
		}
		else if(e.getActionCommand().equals("Undo")||e.getSource()==bundo){
			try{
				panel[jtp.getSelectedIndex()].undo();
			}
			catch(CannotRedoException cre){
				cre.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals("Redo")||e.getSource()==bredo){
			try{
				panel[jtp.getSelectedIndex()].redo();
			}
			catch(CannotRedoException cre){
				cre.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals("Find...")||e.getSource()==bfind)
			panel[jtp.getSelectedIndex()].search();
		else if(e.getActionCommand().equals("Replace...")||e.getSource()==breplace){
			Replaceclass robj=new Replaceclass(panel[jtp.getSelectedIndex()].getLog(),this);}
		

	}
	public void openDocument() throws IOException{
		int size;
		boolean flag=false;
		int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                	File files;
			files = fc.getSelectedFile();
			tabno=jtp.getTabCount();
			for(int i=0;i<jtp.getTabCount();i++){
				if((panel[i].getFile()!=null)&&((panel[i].getFile().getPath()).equals(files.getPath()))){
					flag=true;
					jtp.setSelectedIndex(i);
					break;
				}
			}
			if(!flag){
				panel[tabno]=new DocumentPanel(files,this);

        			jtp.addTab(files.getName(),icon,panel[tabno],"Name : ~"+files.getPath());
				jtp.setTabComponentAt(tabno,new ButtonTabComponent(jtp));
				jtp.setSelectedIndex(tabno);
				setTitle(jtp.getTitleAt(tabno)+"(~"+jtp.getToolTipTextAt(tabno)+") - JVEdit");
			}
		}
	}
	public void newDocument(){
		tabno=jtp.getTabCount();
		panel[tabno]=new DocumentPanel(null,this);
       		jtp.addTab("Untitled Document",icon,panel[tabno],"Blank Document");
		jtp.setTabComponentAt(tabno,new ButtonTabComponent(jtp));
		jtp.setSelectedIndex(tabno);
	}
	public void setTabTitle(){
		jtp.setTitleAt(jtp.getSelectedIndex(),"*"+ jtp.getTitleAt(jtp.getSelectedIndex()));

	}

	public static void main(String[] args) {
      		TextEditor obj = new TextEditor();
      		obj.setVisible(true);
	}
}

class DocumentPanel extends JPanel implements ActionListener  {
		
	JTextArea log;
	File file;
	JScrollPane sp;
	Clipboard clipboard;
	protected UndoManager undoManager;
	TextEditor t;
	JPopupMenu popup;
	JMenuItem popcut,popcopy,poppaste,popdelete,popundo,popredo,popselectall;
	boolean flag=true;

	public DocumentPanel(File file, TextEditor tobj) {
		t=tobj;
		setLayout(new BorderLayout());
		log = new JTextArea();
		this.file=file;
		if(file!=null){
		try{
		FileReader fw=new FileReader(file);
		BufferedReader br=new BufferedReader(fw);
		String ss="";
		while((ss=br.readLine())!=null){
			log.append(ss);
			log.append("\n");
		}
		log.setSelectionStart(1);
		log.setSelectionEnd(10);
		log.setSelectionColor(Color.red);
		fw.close();
		}catch(IOException exp){}
		}
		else
			log.setText("");
		sp = new JScrollPane(log);
		add(sp, BorderLayout.CENTER);
		undoManager = new UndoManager();
		clipboard =this.getToolkit().getSystemClipboard();
		log.getDocument().addDocumentListener(
		new DocumentListener(){
		public void insertUpdate(DocumentEvent o){
			if(flag)
				t.setTabTitle();
			flag=false;
		}
		
		public void removeUpdate(DocumentEvent o){
			if(flag)
				t.setTabTitle();
			flag=false;
		}
		
		public void changedUpdate(DocumentEvent o){
			if(flag)
				t.setTabTitle();
			flag=false;
		}
		});
		log.getDocument().addUndoableEditListener(
        	new UndoableEditListener() {
          	public void undoableEditHappened(UndoableEditEvent e) {
            	undoManager.addEdit(e.getEdit());
            	updateButtons();
          }
        });
		popup = new JPopupMenu();
		popundo = new JMenuItem("Undo");
		popundo.addActionListener(this);
                popup.add(popundo);
		popredo = new JMenuItem("Redo");
		popredo.addActionListener(this);
                popup.add(popredo);	
		popup.addSeparator();
		popcut = new JMenuItem("Cut");
		popcut.addActionListener(this);
		popup.add(popcut);
		popcopy = new JMenuItem("Copy");
		popcopy.addActionListener(this);
                popup.add(popcopy);
		poppaste = new JMenuItem("Paste");
		poppaste.addActionListener(this);
                popup.add(poppaste);
		popdelete = new JMenuItem("Delete");
		popdelete.addActionListener(this);
                popup.add(popdelete);	
		popup.addSeparator();
		popselectall = new JMenuItem("Select All");
		popselectall.addActionListener(this);
                popup.add(popselectall);
                MouseListener popupListener = new PopupListener();
                log.addMouseListener(popupListener);	
	}
		
	public File saveAsDocument() throws IOException{
		file=new File("");
		JFileChooser fc=new JFileChooser();
		flag=true;
		int returnVal = fc.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			if (!file.exists()){
  				FileWriter fo= new FileWriter(file);
				String ss=log.getText();
       				fo.write(ss);
       				fo.close();
 			}
		}
		return file;
	}
	
	public File getFile(){return file;}
	public JTextArea getLog(){return log;}
	public File saveDocument() throws IOException{
		flag=true;
		if(file!=null){
			FileWriter fo= new FileWriter(file);
			String ss=log.getText();
       			fo.write(ss);
      			fo.close();
		}
		else
			file=saveAsDocument();
		return file;
	}
	
	
	public void cutSelection(){
		/*System.out.println(log.getSelectionStart());
		System.out.println(log.getSelectionEnd());		
		log.select(log.getSelectionEnd(),log.getSelectionEnd()+10);*/
		String selection = log.getSelectedText();
		if(!selection.equals("")){
			log.cut();
			StringSelection data = new StringSelection(selection);
			clipboard.setContents(data, data);
		}
	}

	public void copySelection(){
		String selection = log.getSelectedText();
		if(!selection.equals("")){
			StringSelection data = new StringSelection(selection);
			clipboard.setContents(data, data);
		}
	}

	public void undo() throws CannotRedoException{
			 undoManager.undo();
			 updateButtons();
		}

	public void redo() throws CannotRedoException{
			 undoManager.redo();
			 updateButtons();
		}
	public void search(){
		String str= JOptionPane.showInputDialog(this, "Search for :","Find",JOptionPane.QUESTION_MESSAGE);
		if(str!=null||(!(str.equals("")))){
		System.out.println(str);
		System.out.println("in func");
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
		for(int j=0;j<val-1;j++){
			log.select(arr[j],arr[j]+str.length());
			log.setSelectedTextColor(Color.magenta);
			System.out.println(arr[j]);}
		}
	}

	 public void updateButtons() {
		t.bundo.setEnabled(undoManager.canUndo());
		t.bredo.setEnabled(undoManager.canRedo());
	}

	public void pasteClipboard() throws IOException, UnsupportedFlavorException{
		Transferable clipData = clipboard.getContents(clipboard);
		if (clipData != null) {
		        if (clipData.isDataFlavorSupported(DataFlavor.stringFlavor)) {
		        	String s = (String)(clipData.getTransferData(DataFlavor.stringFlavor));
		        	log.replaceSelection(s);
		        }
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cut"))
			cutSelection();
		else if(e.getActionCommand().equals("Copy"))
			copySelection();
		else if(e.getActionCommand().equals("Paste")){
			try{
				pasteClipboard();
			}
			catch(Exception excp){
			}
		}
		else if(e.getActionCommand().equals("Undo")){
			try{
				undo();
			}
			catch(CannotRedoException cre){
				cre.printStackTrace();
			}
		}
		else if(e.getActionCommand().equals("Redo")){
			try{
				redo();
			}
			catch(CannotRedoException cre){
				cre.printStackTrace();
			}
		}
		if(e.getActionCommand().equals("Delete"))
			log.replaceSelection("");
		if(e.getActionCommand().equals("Select All"))
			log.selectAll();
	}


	class PopupListener extends MouseAdapter{
		public void mousePressed(MouseEvent e){
			if (e.isPopupTrigger())
        	        	popup.show(e.getComponent(),e.getX(), e.getY());
		}
		public void mouseReleased(MouseEvent e) {
		        if (e.isPopupTrigger())
		                popup.show(e.getComponent(),e.getX(), e.getY());
		}
    	}	
}

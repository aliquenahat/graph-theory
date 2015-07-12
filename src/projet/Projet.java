package projet;

import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Projet extends JFrame implements ActionListener {
	// Liste des attributs
	private static final long serialVersionUID = 1L;
	JFileChooser chooser = new JFileChooser();
	String[] tab = { "Kruskal", "Prim", "Elimination de cycles" };
	private JComboBox combo = new JComboBox(tab);
	private JPanel pan = new JPanel();
	private JButton parcourir = new JButton("Parcourir...");
	private JButton bouton = new JButton("Calculer");
	ImageIcon images = new ImageIcon("image.png");
	private JLabel image = new JLabel(images);
	private JLabel label = new JLabel(
			"Il vous faut sélectionner un fichier de type .dat ou .txt.");
	private JLabel label2 = new JLabel("");
	private JTextField champ = new JTextField(13);
	FileFilter filtre = new FileNameExtensionFilter("Texte", "txt", "dat");

	public Projet() throws FileNotFoundException {
		// Préparation de la fenêtre
		this.setTitle("NF20 - Calcul du coût minimal");
		this.setSize(470, 330);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		chooser.addChoosableFileFilter(filtre);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filtre);
		parcourir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selection = chooser.getSelectedFile();
					champ.setText(selection.getAbsolutePath());
				}
			}
		});
		bouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File selection = chooser.getSelectedFile();
				Graphes graphe = null;
				try {
					graphe = new Graphes(selection.getAbsolutePath());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (selection.getAbsolutePath() != null) {
					if (graphe.getOriente() == false) {
						if (graphe.getConnexe() == true) {
							if (combo.getSelectedItem() == "Kruskal") {
								graphe.kruskal();
							} else if (combo.getSelectedItem() == "Prim") {
								graphe.prim();
							} else if (combo.getSelectedItem() == "Elimination de cycles") {
								graphe.elimination();
							}
							label.setText("<html><center>Le résultat de la liste des arêtes est diponible en lançant avec Eclipse.<br/><br/>Coût total minimal : <b>"
									+ graphe.getCoutTotal()
									+ "</b> avec un temps calculé de <b>"
									+ Float.toString(graphe.getSeconds())
									+ "</b>ms</html>");
							label2.setText("<html>Nombre de sommets : <b>"
									+ graphe.getNbSommets()
									+ "</b> Nombre d'arêtes : <b>"
									+ graphe.getNbAretes()
									+ "</b></center><br/></html>");
						} else {
							label.setText("Ce n'est pas un graphe connexe. Réessayez avec un autre.");
							label2.setText("");
							System.out
									.println("Ce n'est pas un graphe connexe. Arrêt du programme.");
						}

					} else {
						label.setText("Ce n'est pas un graphe ou alors ce n'est pas un graphe non orienté.");
						label2.setText("");
						System.out
								.println("Ce n'est pas un graphe ou alors ce n'est pas un non oriente. Arrêt du programme.");
					}
				}

			}

		});
		// Ajout du bouton à notre content pane
		pan.add(image);
		pan.add(label);
		pan.add(label2);
		pan.add(champ);
		pan.add(parcourir);
		pan.add(combo);
		pan.add(bouton);
		this.setContentPane(pan);
		this.setVisible(true);
		this.setResizable(false);
	}

	public static void main(String[] args) throws FileNotFoundException {
		@SuppressWarnings("unused")
		Projet fenetre = new Projet();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
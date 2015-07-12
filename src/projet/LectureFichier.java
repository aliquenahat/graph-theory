package projet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LectureFichier {
	private String buffer;
	private String fichier;

	public LectureFichier(String fichier) {
		this.fichier = fichier;
		this.buffer = "";
	}

	public String read() {
		// lecture du fichier texte
		try {
			InputStream ips = new FileInputStream(fichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine()) != null) {

				this.buffer += ligne + "\n";
			}

			br.close();
			return buffer;
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}
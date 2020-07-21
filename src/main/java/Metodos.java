import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public abstract class Metodos {

	public static String seleccionarVideo() {

		String archivo = "";
		File[] files = seleccionar(2, "Video", "Elije un archivo de video");

		if (files != null && files.length > 0) {

			try {
				archivo = files[0].getCanonicalPath();
			}

			catch (IOException e1) {
				//
			}

		}

		return archivo;
	}

	public static String saberSeparador(String os) {

		if (os.equals("Linux")) {
			return "/";
		}

		else {
			return "\\";
		}

	}

	public static String extraerExtension(String nombreArchivo) {

		String extension = "";

		if (nombreArchivo.length() >= 3) {

			extension = nombreArchivo.substring(nombreArchivo.length() - 3, nombreArchivo.length());

			extension = extension.toLowerCase();

			if (extension.equals("peg")) {
				extension = "jpeg";
			}

			if (extension.equals("fif")) {
				extension = "jfif";
			}

			if (extension.equals("ebp")) {
				extension = "webp";
			}

			if (extension.equals("ebm")) {
				extension = "webm";
			}

			if (extension.equals("3u8")) {
				extension = "m3u8";
			}

			if (extension.equals(".ts")) {
				extension = "ts";
			}

		}

		return extension;
	}

	public static String eliminarPuntos(String cadena) {

		String cadena2 = cadena;

		try {

			cadena2 = cadena.substring(0, cadena.length() - 4);

			cadena = cadena2.replace(".", "_") + "." + extraerExtension(cadena);

			cadena = eliminarTodosLosEspacios(cadena);

		} catch (Exception e) {

		}

		return cadena;
	}

	public static String eliminarTodosLosEspacios(String cadena) {

		cadena = cadena.trim();

		cadena = cadena.replace("  ", " ");

		cadena = cadena.trim();

		cadena = cadena.replace(" ", "_");

		return cadena;
	}

	public static void abrirCarpeta(String ruta) throws IOException {

		if (ruta != null && !ruta.equals("") && !ruta.isEmpty()) {

			try {

				if (VideoFrames.getOs().contentEquals("Linux")) {
					Runtime.getRuntime().exec("xdg-open " + ruta);
				}

				else {
					Runtime.getRuntime().exec("cmd /c explorer " + "\"" + ruta + "\"");
				}

			}

			catch (IOException e) {
				mensaje("Ruta inválida", 1);
			}

		}

	}

	public static void mensaje(String mensaje, int titulo) {

		String tituloSuperior = "";

		int tipo = 0;

		switch (titulo) {

		case 1:
			tipo = JOptionPane.ERROR_MESSAGE;
			tituloSuperior = "Error";

			break;

		case 2:
			tipo = JOptionPane.INFORMATION_MESSAGE;
			tituloSuperior = "Informacion";

			break;

		case 3:
			tipo = JOptionPane.WARNING_MESSAGE;
			tituloSuperior = "Advertencia";

			break;

		default:
			break;

		}

		JLabel alerta = new JLabel(mensaje);

		alerta.setFont(new Font("Arial", Font.BOLD, 18));

		JOptionPane.showMessageDialog(null, alerta, tituloSuperior, tipo);

	}

	public static void renombrar(String ruta1, String ruta2) {

		File f1 = new File(ruta1);
		File f2 = new File(ruta2);

		f1.renameTo(f2);

	}

	public static LinkedList<String> directorio(String ruta, String extension, boolean filtro) {

		LinkedList<String> lista = new LinkedList<String>();

		File f = new File(ruta);

		if (f.exists()) {

			lista.clear();

			File[] ficheros = f.listFiles();

			String fichero = "";

			String extensionArchivo;

			File folder;

			for (int x = 0; x < ficheros.length; x++) {

				fichero = ficheros[x].getName();

				folder = new File(ruta + fichero);

				if (filtro) {

					if (folder.isFile()) {

						extensionArchivo = extraerExtension(fichero);

						if (fichero.contains(" ")
								|| fichero.length() > 5 && fichero.substring(0, fichero.length() - 5).contains(".")) {

							renombrar(ruta + fichero, ruta + eliminarPuntos(fichero));

						}

						if (extension.equals("webp") && extensionArchivo.equals("webp")
								|| extension.equals("jpeg") && extensionArchivo.equals("jpeg") || extension.equals(".")
								|| extension.equals(extensionArchivo)) {

							lista.add(fichero);
						}

					}

				}

				else {

					if (folder.isDirectory()) {
						lista.add(fichero);
					}

				}

			}

		}

		return lista;

	}

	public static java.io.File[] seleccionar(int tipo, String rotulo, String mensaje) {

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = null;

		switch (tipo) {

		case 1:
			filter = new FileNameExtensionFilter(rotulo, "jpg");
			chooser.setFileFilter(filter);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			break;

		case 2:
			filter = new FileNameExtensionFilter(rotulo, "jpg", "gif", "jpeg", "png", "avi", "mp4");
			chooser.setFileFilter(filter);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			break;

		default:
			break;

		}

		if (!chooser.isMultiSelectionEnabled()) {
			chooser.setMultiSelectionEnabled(true);
		}

		chooser.showOpenDialog(chooser);
		File[] files = chooser.getSelectedFiles();

		return files;
	}

}

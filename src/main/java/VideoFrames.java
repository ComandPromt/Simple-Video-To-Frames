
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.TooManyListenersException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("all")

public class VideoFrames extends javax.swing.JFrame implements ActionListener, ChangeListener {

	LinkedList<String> archivos = new LinkedList<String>();

	static String os = System.getProperty("os.name");

	static String separador = Metodos.saberSeparador(os);

	JCheckBox conversionMultiple = new JCheckBox("Extraer frames de todos los videos de la carpeta");

	String archivo = "";

	private JTextField directorioGuardar;

	public static String getSeparador() {
		return separador;
	}

	public VideoFrames() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(VideoFrames.class.getResource("/imagenes/video_2_frame.png")));

		setTitle("Video To Frames");

		initComponents();

		try {
			directorioGuardar.setText(new File(".").getCanonicalPath());
		}

		catch (IOException e) {
			//
		}

		this.setVisible(true);
	}

	public static String getOs() {
		return os;
	}

	private void initComponents() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		setResizable(false);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(VideoFrames.class.getResource("/imagenes/import.png")));

		lblNewLabel.addMouseListener(new MouseAdapter() {

			@Override

			public void mousePressed(MouseEvent e) {

				archivo = Metodos.seleccionarVideo();

			}

		});

		JTextArea imagenes = new JTextArea();
		imagenes.setText("  Arrastra los archivos aqui");
		imagenes.setForeground(Color.DARK_GRAY);
		imagenes.setFont(new Font("Tahoma", Font.BOLD, 24));
		imagenes.setEditable(false);
		imagenes.setBackground(Color.WHITE);

		JButton btnNewButton = new JButton("Convertir");
		btnNewButton.setFont(new Font("Dialog", Font.BOLD, 16));
		btnNewButton.setIcon(new ImageIcon(VideoFrames.class.getResource("/imagenes/video_2_frame.png")));

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				try {

					String directorioAGuardar = directorioGuardar.getText();

					String carpetaSalida = "";

					if (!archivo.isEmpty() && !directorioAGuardar.isEmpty()) {

						if (!conversionMultiple.isSelected()) {

							String nombreArchivo = archivo;

							carpetaSalida = directorioAGuardar + separador
									+ nombreArchivo.substring(nombreArchivo.lastIndexOf(separador) + 1,
											nombreArchivo.lastIndexOf("."))
									+ separador + nombreArchivo.substring(nombreArchivo.lastIndexOf(separador) + 1,
											nombreArchivo.length());

							Metodos.crearCarpeta(carpetaSalida.substring(0, carpetaSalida.lastIndexOf(separador)));

							Runtime.getRuntime()
									.exec("ffmpeg -i \"" + archivo + "\" \"" + carpetaSalida + "_%06d.png\"");

							Metodos.borrarArchivosDuplicados(carpetaSalida);

							directorioAGuardar = carpetaSalida.substring(0, carpetaSalida.lastIndexOf(separador));

						}

						else {

							LinkedList<String> comprobacion = new LinkedList<String>();

							String carpetaArchivo = archivo;

							carpetaArchivo = carpetaArchivo.substring(0, carpetaArchivo.lastIndexOf(separador) + 1);

							archivos.clear();

							archivos = Metodos.directorio(carpetaArchivo, "avi", true);

							comprobacion = Metodos.directorio(carpetaArchivo, "mp4", true);

							comprobarVideos(comprobacion);

							comprobacion = Metodos.directorio(carpetaArchivo, "mkv", true);

							comprobarVideos(comprobacion);

							comprobacion = Metodos.directorio(carpetaArchivo, "mpg", true);

							comprobarVideos(comprobacion);

							for (int i = 0; i < archivos.size(); i++) {

								String nombreArchivo = archivos.get(i);

								carpetaSalida = directorioAGuardar + separador
										+ nombreArchivo.substring(nombreArchivo.lastIndexOf(separador) + 1,
												nombreArchivo.lastIndexOf("."))
										+ separador + nombreArchivo.substring(nombreArchivo.lastIndexOf(separador) + 1,
												nombreArchivo.length());

								Metodos.crearCarpeta(carpetaSalida.substring(0, carpetaSalida.lastIndexOf(separador)));

								Runtime.getRuntime().exec("ffmpeg -i \"" + carpetaArchivo + archivos.get(i) + "\" \""
										+ carpetaSalida + "_%06d.png\"");

								Metodos.borrarArchivosDuplicados(carpetaSalida);

							}

						}

					}

					int resp = JOptionPane.showConfirmDialog(null, "¿Desea abrir la carpeta de salida?");

					if (resp == 0) {

						Metodos.abrirCarpeta(directorioAGuardar);

					}

				}

				catch (Exception e) {
					e.printStackTrace();

				}

			}

			private void comprobarVideos(LinkedList<String> comprobacion) {

				if (!comprobacion.isEmpty()) {

					for (int i = 0; i < comprobacion.size(); i++) {
						archivos.add(comprobacion.get(i));
					}

				}
			}

		});

		JLabel lblNewLabel_1 = new JLabel("Guardar en");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 18));

		directorioGuardar = new JTextField();
		directorioGuardar.setToolTipText("");
		directorioGuardar.setHorizontalAlignment(SwingConstants.CENTER);
		directorioGuardar.setFont(new Font("Tahoma", Font.PLAIN, 16));

		JButton btnNewButton_1 = new JButton("");

		btnNewButton_1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				File[] files = Metodos.seleccionar(1, "Elija la carpeta de salida", "Elija la carpeta de salida");

				try {
					directorioGuardar.setText(files[0].getCanonicalPath());
				}

				catch (Exception e1) {

				}

			}

		});

		btnNewButton_1.setIcon(new ImageIcon(VideoFrames.class.getResource("/imagenes/abrir.png")));

		conversionMultiple.setFont(new Font("Dialog", Font.BOLD, 14));

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createParallelGroup(Alignment.TRAILING)
				.addGroup(layout.createSequentialGroup().addContainerGap(176, Short.MAX_VALUE)
						.addComponent(btnNewButton).addGap(139))
				.addGroup(layout.createSequentialGroup().addGap(37).addGroup(layout
						.createParallelGroup(Alignment.LEADING)
						.addComponent(conversionMultiple, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
								.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(imagenes, GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)))
						.addGroup(layout.createSequentialGroup()
								.addComponent(
										btnNewButton_1, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
								.addGap(37)
								.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblNewLabel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 325,
												Short.MAX_VALUE)
										.addComponent(directorioGuardar, 325, 325, 325))))
						.addGap(39)));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup()
				.addGap(21)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
						.addGroup(layout.createSequentialGroup().addComponent(lblNewLabel_2).addGap(18)
								.addComponent(imagenes, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)))
				.addGap(25).addComponent(lblNewLabel_1)
				.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(Alignment.TRAILING)
						.addComponent(directorioGuardar, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE))
				.addGap(29).addComponent(btnNewButton).addGap(18).addComponent(conversionMultiple).addGap(105)));

		getContentPane().setLayout(layout);
		setSize(new Dimension(490, 403));
		setLocationRelativeTo(null);

		javax.swing.border.TitledBorder dragBorder = new javax.swing.border.TitledBorder("Drop 'em");

		try {

			new DragAndDrop(imagenes, dragBorder, rootPaneCheckingEnabled, new DragAndDrop.Listener() {

				public void filesDropped(java.io.File[] files) {

					try {
						archivo = files[0].getCanonicalPath();
					}

					catch (IOException e1) {
						//
					}

				}

			});

		}

		catch (TooManyListenersException e1) {
			//
		}

	}

	public void stateChanged(ChangeEvent arg0) {
		//

	}

	public void actionPerformed(ActionEvent arg0) {
		//

	}
}

package gui.panels;

import gui.Main;
import gui.components.panels.MangaHeader;
import gui.components.panels.VolumeCard;
import gui.dialogs.Dialog;
import gui.dialogs.MangaDialog;
import gui.dialogs.VolumeDialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import utils.ExceptionUtils;
import model.Manga;
import model.Volume;
import database.dao.MangaDAO;
import database.dao.VolumeDAO;

public class VolumesPanel extends JPanel
{

	private JPanel panelVolumes;
	private JPanel centerPanel;

	private Manga manga;

	private static final String PANEL_CARDS = "cards", PANEL_TABLE = "table";
	private JTable tableVolumes;
	private MangaHeader mhManga;

	public VolumesPanel(Manga manga)
	{
		this.manga = manga;
		setLayout(new BorderLayout(0, 0));

		JPanel panelNorth = new JPanel();
		add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));

		mhManga = new MangaHeader(manga);
		mhManga.addEditButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MangaDialog lMangaDialog = new MangaDialog(mhManga.getManga());
				if (lMangaDialog.showDialog() == Dialog.APPROVE_OPTION)
				{
					try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
					{
						if (!lMangaDAO.update(lMangaDialog.getResult()))
							JOptionPane.showMessageDialog(null, "Houve um erro ao editar o mang�.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
						mhManga.setManga(lMangaDialog.getResult());
					}
					catch (SQLException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
					Main.MANGAS_PANEL.fillMangas();
				}
			}
		});
		mhManga.addRemoveButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (JOptionPane.showConfirmDialog(null, "Voc� tem certeza que deseja remover este mang�?\nTodos os itens relacionados a este mang� ser�o removidos tamb�m.", "Confirma��o de exclus�o", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
					{
						if (!lMangaDAO.remove(mhManga.getManga()))
							JOptionPane.showMessageDialog(null, "Houve um erro ao deletar o mang�.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
					}
					catch (SQLException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
					Main.MANGAS_PANEL.fillMangas();
					Main.showPanel(Panels.MANGAS_LIST);
				}
			}
		});
		panelNorth.add(mhManga);

		JToolBar jtOptions = new JToolBar();
		jtOptions.setFloatable(false);
		panelNorth.add(jtOptions, BorderLayout.NORTH);

		JToggleButton btCards = new JToggleButton("");
		btCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				CardLayout lCardLayout = (CardLayout) centerPanel.getLayout();
				lCardLayout.show(centerPanel, PANEL_CARDS);
			}
		});
		btCards.setToolTipText("Mostrar cards");
		btCards.setSelected(true);
		btCards.setPreferredSize(new Dimension(32, 32));
		btCards.setIcon(new ImageIcon(VolumesPanel.class.getResource("/images/cards_16.png")));
		jtOptions.add(btCards);

		JToggleButton btTable = new JToggleButton("");
		btTable.setToolTipText("Mostrar tabela");
		btTable.setPreferredSize(new Dimension(32, 32));
		btTable.setIcon(new ImageIcon(VolumesPanel.class.getResource("/images/table_16.png")));
		btTable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				CardLayout lCardLayout = (CardLayout) centerPanel.getLayout();
				lCardLayout.show(centerPanel, PANEL_TABLE);
			}
		});
		jtOptions.add(btTable);

		ButtonGroup lButtonGroup = new ButtonGroup();
		lButtonGroup.add(btCards);
		lButtonGroup.add(btTable);

		Component horizontalGlue = Box.createHorizontalGlue();
		jtOptions.add(horizontalGlue);

		JButton btNewVolume = new JButton("Novo");
		btNewVolume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newVolume();
			}
		});
		jtOptions.add(btNewVolume);

		centerPanel = new JPanel();
		centerPanel.setLayout(new CardLayout());
		add(centerPanel, BorderLayout.CENTER);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		centerPanel.add(scrollPane_1, PANEL_CARDS);

		panelVolumes = new JPanel();
		scrollPane_1.setViewportView(panelVolumes);
		panelVolumes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelVolumes.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				int horizontalCards = ((int) getSize().getWidth() - 15) / 300;
				int divide = (int) Math.ceil((double) manga.getVolumes().size() / horizontalCards);
				panelVolumes.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 150 * divide + 5 * divide));
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, PANEL_TABLE);

		tableVolumes = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		tableVolumes.getTableHeader().setReorderingAllowed(false);
		tableVolumes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableVolumes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(tableVolumes);

		fillVolumes();
	}

	private void newVolume()
	{
		VolumeDialog lVolumeDialog = new VolumeDialog(manga);
		if (lVolumeDialog.showDialog() == Dialog.APPROVE_OPTION)
		{
			try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
			{
				Volume result = lVolumeDialog.getResult();
				if (!lVolumeDAO.insert(result))
					JOptionPane.showMessageDialog(null, "Houve um erro ao inserir o volume.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);

			}
			catch (SQLException e)
			{
				ExceptionUtils.showExceptionDialog(null, e);
			}
			fillVolumes();
		}
	}

	private void fillVolumes()
	{

		try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
		{
			manga = lMangaDAO.select(manga.getId());
			mhManga.setManga(manga);
			Main.MANGAS_PANEL.fillMangas();
			fillCards();
			fillTable();
		}
		catch (SQLException e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}

	}

	private void fillCards()
	{
		panelVolumes.removeAll();
		for (Volume v : manga.getVolumes())
		{
			VolumeCard lVolumeCard = new VolumeCard(v);
			lVolumeCard.addEditButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					VolumeDialog lVolumeDialog = new VolumeDialog(lVolumeCard.getVolume());
					if (lVolumeDialog.showDialog() == Dialog.APPROVE_OPTION)
					{
						try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
						{
							if (!lVolumeDAO.update(lVolumeDialog.getResult()))
								JOptionPane.showMessageDialog(null, "Houve um erro ao editar o volume.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
							lVolumeCard.setVolume(lVolumeDialog.getResult());
						}
						catch (SQLException e)
						{
							ExceptionUtils.showExceptionDialog(null, e);
						}
					}
				}
			});
			lVolumeCard.addRemoveButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if (JOptionPane.showConfirmDialog(null, "Voc� tem certeza que deseja remover este volume?\nTodos os itens relacionados a este volume ser�o removidos tamb�m.", "Confirma��o de exclus�o", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
						{
							if (!lVolumeDAO.remove(lVolumeCard.getVolume()))
								JOptionPane.showMessageDialog(null, "Houve um erro ao deletar o volume.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
						}
						catch (SQLException e)
						{
							ExceptionUtils.showExceptionDialog(null, e);
						}
						fillVolumes();
					}
				}
			});
			panelVolumes.add(lVolumeCard);
		}
		panelVolumes.revalidate();
		panelVolumes.repaint();

		int horizontalCards = ((getSize().getWidth() == 0 ? 958 : (int) getSize().getWidth()) - 15) / 300;
		int divide = (int) Math.ceil((double) manga.getVolumes().size() / horizontalCards);
		panelVolumes.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 150 * divide + 5 * divide));
	}

	private void fillTable()
	{
		DefaultTableModel lDefaultTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		lDefaultTableModel.addColumn("ID");
		lDefaultTableModel.addColumn("#");
		lDefaultTableModel.addColumn("T�tulo");
		lDefaultTableModel.addColumn("ISBN");
		lDefaultTableModel.addColumn("Editora");
		lDefaultTableModel.addColumn("Pre�o Total");
		lDefaultTableModel.addColumn("Tamanho");

		for (Volume v : manga.getVolumes())
			lDefaultTableModel.addRow(new Object[] { v.getId(), v.getNumber(), v.getTitle(), v.getIsbn(), v.getPublisher().getName(), v.getTotalPrice(), v.getSize() });

		tableVolumes.setModel(lDefaultTableModel);
	}

}

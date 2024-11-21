/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com_2311135.view;

import com_2311135.controller.GastoController;
import com_2311135.repository.GastoRepository;
import com_2311135.model.Gastos;
import com_2311135.service.GastosService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GastoUI extends JFrame {
    private final GastoController controller;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public GastoUI(GastoController controller) {
        this.controller = controller;

        setTitle("Gestión de Gastos Personales");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabla de gastos
        String[] columnNames = {"ID", "Descripción", "Categoría", "Monto", "Fecha"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Agregar Gasto");
        JButton editButton = new JButton("Editar Gasto");
        JButton deleteButton = new JButton("Eliminar Gasto");
        JButton summaryButton = new JButton("Resumen");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(summaryButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Cargar los datos de la tabla al iniciar
        loadGastos();

        // Acción para agregar un gasto
        addButton.addActionListener(e -> {
            new GastosForm(this, "Registrar Gasto", controller, null).setVisible(true);
            loadGastos(); // Recargar la tabla después de agregar
        });

        // Acción para editar un gasto
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // Verificar fila seleccionada
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0); // Obtener el ID del gasto
                Gastos gastoToEdit = controller.getAllGastos().stream()
                        .filter(g -> g.getId() == id)
                        .findFirst()
                        .orElse(null);

                if (gastoToEdit != null) {
                    new GastosForm(this, "Editar Gasto", controller, gastoToEdit).setVisible(true);
                    loadGastos(); // Recargar la tabla después de editar
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un gasto para editar.");
            }
        });

        // Acción para eliminar un gasto
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow(); // Verificar fila seleccionada
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0); // Obtener el ID del gasto

                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar este gasto?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    controller.deleteGasto(id); // Eliminar el gasto
                    loadGastos(); // Recargar la tabla después de eliminar
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un gasto para eliminar.");
            }
        });

        // Acción para mostrar el resumen de gastos
        summaryButton.addActionListener(e -> {
            String resumen = controller.getResumenGastos();
            JOptionPane.showMessageDialog(this, resumen);
        });
    }

    // Método para cargar y mostrar los datos de los gastos en la tabla
    private void loadGastos() {
        List<Gastos> gastoos = controller.getAllGastos();
        tableModel.setRowCount(0); // Limpiar la tabla
        for (Gastos gastos : gastoos) {
            tableModel.addRow(new Object[]{
                    gastos.getId(),
                    gastos.getDescripcion(),
                    gastos.getCategoria(),
                    gastos.getMonto(),
                    gastos.getFechaGasto()
            });
        }
    }
}
        


package com.javafx.final_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.scene.control.TableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;


import java.util.Optional;

public class HelloController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private TableView<Cinema> tableView;
    @FXML
    private TableColumn<Cinema, String> nameColumn;
    @FXML
    private TableColumn<Cinema, Integer> quantityColumn;
    @FXML
    private TableColumn<Cinema, Double> priceColumn;
    @FXML
    private TableColumn<Cinema, Double> totalColumn;
    @FXML
    private TableColumn<Cinema, Void> actionColumn;
    @FXML
    private Button submitButton;
    @FXML
    private Label totalLabel;

    private final ObservableList<Cinema> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        Callback<TableColumn<Cinema, Void>, TableCell<Cinema, Void>> cellFactory = new Callback<TableColumn<Cinema, Void>, TableCell<Cinema, Void>>() {
            @Override
            public TableCell<Cinema, Void> call(final TableColumn<Cinema, Void> param) {
                final TableCell<Cinema, Void> cell = new TableCell<Cinema, Void>() {

                    private final Button btn = new Button("Delete");

                    {
                        btn.setOnAction((event) -> {
                            Cinema item = getTableView().getItems().get(getIndex());
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Dialog");
                            alert.setHeaderText(null);
                            alert.setContentText("Are you sure you want to remove this item?");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                data.remove(item);
                                updateTotal();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);

        tableView.setItems(data);
        tableView.setEditable(true);

        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            Cinema item = event.getRowValue();
            item.setQuantity(event.getNewValue());
            item.setTotal(item.getQuantity() * item.getPrice());
            tableView.refresh();
            updateTotal();
        });

        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceColumn.setOnEditCommit(event -> {
            Cinema item = event.getRowValue();
            item.setPrice(event.getNewValue());
            item.setTotal(item.getQuantity() * item.getPrice());
            tableView.refresh();
            updateTotal();
        });
    }

    @FXML
    protected void handleSubmit() {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());
        double total = quantity * price;

        Cinema newItem = new Cinema(name, quantity, price, total);
        data.add(newItem);

        nameField.clear();
        quantityField.clear();
        priceField.clear();

        updateTotal();
    }

    private void updateTotal() {
        double total = data.stream().mapToDouble(Cinema::getTotal).sum();
        totalLabel.setText("₱ " + String.format("%.2f", total));
    }

    public static class Cinema {
        private final String name;
        private int quantity;
        private double price;
        private double total;

        public Cinema(String name, int quantity, double price, double total) {
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.total = total;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    }
}
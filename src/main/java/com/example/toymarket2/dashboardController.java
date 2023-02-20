package com.example.toymarket2;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class dashboardController implements Initializable {

    @FXML
    private AnchorPane main_form;

    @FXML
    private Label username;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button availableProduct_btn;

    @FXML
    private Button purchase_btn;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Label dashboard_AB;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_TC;

    @FXML
    private AreaChart<?, ?> dashboard_incomeChart;

    @FXML
    private BarChart<?, ?> dashboard_customerChart;

    @FXML
    private AnchorPane availableProduct_form;

    @FXML
    private ImageView availableProduct_imageView;

    @FXML
    private TextField availableProduct_productID;

    @FXML
    private TextField availableProduct_brnd;

    @FXML
    private TextField availableProduct_name;

    @FXML
    private TextField availableProduct_country;

    @FXML
    private DatePicker availableProduct_date;

    @FXML
    private TextField availableProduct_price;

    @FXML
    private TextField availableProduct_search;

    @FXML
    private TableView<ToyData> availableProduct_tableView;

    @FXML
    private TableColumn<ToyData, String> availablePr_col_ID;

    @FXML
    private TableColumn<ToyData, String> availablePr_col_brand;

    @FXML
    private TableColumn<ToyData, String> availablePr_col_name;

    @FXML
    private TableColumn<ToyData, String> availablePr_col_cntr;

    @FXML
    private TableColumn<ToyData, String> availablePr_col_date;

    @FXML
    private TableColumn<ToyData, String> availablePr_col_price;

    @FXML
    private AnchorPane purchase_form;

    @FXML
    private ComboBox<?> purchase_ProductID;

    @FXML
    private ComboBox<?> purchase_ProdctName;

    @FXML
    private Label purchase_total;

    @FXML
    private Label purchase_info_ID;

    @FXML
    private Label purchase_info_name;

    @FXML
    private Label purchase_info_brand;

    @FXML
    private Label purchase_info_country;

    @FXML
    private Label purchase_info_date;

    @FXML
    private TableView<customerData> purchase_tableView;

    @FXML
    private Spinner<Integer> purchase_quantity;

    @FXML
    private TableColumn<customerData, String> purchase_col_bookID;

    @FXML
    private TableColumn<customerData, String> purchase_col_bookTitle;

    @FXML
    private TableColumn<customerData, String> purchase_col_author;

    @FXML
    private TableColumn<customerData, String> purchase_col_genre;

    @FXML
    private TableColumn<customerData, String> purchase_col_quantity;

    @FXML
    private TableColumn<customerData, String> purchase_col_price;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;

    public void dashboardAB() {

        String sql = "SELECT COUNT(id) FROM toy";

        connect = database.connectDb();
        int countAB = 0;
        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                countAB = result.getInt("COUNT(id)");
            }

            dashboard_AB.setText(String.valueOf(countAB));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTI() {

        String sql = "SELECT SUM(total) FROM customer_info";

        connect = database.connectDb();
        double sumTotal = 0;
        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                sumTotal = result.getDouble("SUM(total)");
            }

            dashboard_TI.setText("₽" + sumTotal);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTC() {
        String sql = "SELECT COUNT(id) FROM customer_info";

        connect = database.connectDb();
        int countTC = 0;
        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                countTC = result.getInt("COUNT(id)");
            }

            dashboard_TC.setText(String.valueOf(countTC));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void dashboardIncomeChart() {

        dashboard_incomeChart.getData().clear();

        String sql = "SELECT date, SUM(total) FROM customer_info GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 6";

        connect = database.connectDb();

        try {
            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            dashboard_incomeChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void dashboardCustomerChart() {

        dashboard_customerChart.getData().clear();

        String sql = "SELECT date, COUNT(id) FROM customer_info GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 4";

        connect = database.connectDb();

        try {
            XYChart.Series chart = new XYChart.Series();

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {
                chart.getData().add(new XYChart.Data(result.getString(1), result.getInt(2)));
            }

            dashboard_customerChart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void availableProductsAdd() {

        String sql = "INSERT INTO toy (book_id, title, author, genre, pub_date, price, image) "
                + "VALUES(?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableProduct_productID.getText().isEmpty()
                    || availableProduct_brnd.getText().isEmpty()
                    || availableProduct_name.getText().isEmpty()
                    || availableProduct_country.getText().isEmpty()
                    || availableProduct_date.getValue() == null
                    || availableProduct_price.getText().isEmpty()
                    || getData.path == null || getData.path.equals("")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                String checkData = "SELECT book_id FROM toy WHERE book_id = '"
                        + availableProduct_productID.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Ошибка!");
                    alert.setHeaderText(null);
                    alert.setContentText("Товар с ID: " + availableProduct_productID.getText() + " уже существует!");
                    alert.showAndWait();
                } else {

                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, availableProduct_productID.getText());
                    prepare.setString(2, availableProduct_brnd.getText());
                    prepare.setString(3, availableProduct_name.getText());
                    prepare.setString(4, availableProduct_country.getText());
                    prepare.setString(5, String.valueOf(availableProduct_date.getValue()));
                    prepare.setString(6, availableProduct_price.getText());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    prepare.setString(7, uri);

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Информационное сообщение");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно добавлено!");
                    alert.showAndWait();


                    availableBooksShowListData();

                    availableProductClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void availableProductUpdate() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE toy SET title = '"
                + availableProduct_brnd.getText() + "', author = '"
                + availableProduct_name.getText() + "', genre = '"
                + availableProduct_country.getText() + "', pub_date = '"
                + availableProduct_date.getValue() + "', price = '"
                + availableProduct_price.getText() + "', image = '"
                + uri + "' WHERE book_id = '" + availableProduct_productID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableProduct_productID.getText().isEmpty()
                    || availableProduct_brnd.getText().isEmpty()
                    || availableProduct_name.getText().isEmpty()
                    || availableProduct_country.getText().isEmpty()
                    || availableProduct_date.getValue() == null
                    || availableProduct_price.getText().isEmpty()
                    || getData.path == null || getData.path.equals("")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все пустые поля");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Сообщение с подтверждением");
                alert.setHeaderText(null);
                alert.setContentText("Вы действительно хотите обновить товар с ID: " + availableProduct_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Информационное сообщение");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешное обновление!");
                    alert.showAndWait();


                    availableBooksShowListData();

                    availableProductClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void availableProductDelete() {

        String sql = "DELETE FROM toy WHERE book_id = '"
                + availableProduct_productID.getText() + "'";

        connect = database.connectDb();

        try {
            Alert alert;

            if (availableProduct_productID.getText().isEmpty()
                    || availableProduct_brnd.getText().isEmpty()
                    || availableProduct_name.getText().isEmpty()
                    || availableProduct_country.getText().isEmpty()
                    || availableProduct_date.getValue() == null
                    || availableProduct_price.getText().isEmpty()
                    || getData.path == null || getData.path.equals("")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Пожалуйста, заполните все пустые поля");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Сообщение с подтверждением");
                alert.setHeaderText(null);
                alert.setContentText("Вы действительно хотите удалить товар с ID: " + availableProduct_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Информационное сообщение");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешное удаление!");
                    alert.showAndWait();


                    availableBooksShowListData();

                    availableProductClear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void availableProductClear() {
        availableProduct_productID.setText("");
        availableProduct_brnd.setText("");
        availableProduct_name.setText("");
        availableProduct_country.setText("");
        availableProduct_date.setValue(null);
        availableProduct_price.setText("");

        getData.path = "";

        availableProduct_imageView.setImage(null);
    }

    public void avaialableProductInsertImage() {

        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new ExtensionFilter("File Image", "*jpg", "*png"));

        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 112, 137, false, true);
            availableProduct_imageView.setImage(image);
        }

    }

    public ObservableList<ToyData> availableBooksListData() {

        ObservableList<ToyData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM toy";

        connect = database.connectDb();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ToyData bookD;

            while (result.next()) {
                bookD = new ToyData(result.getInt("book_id"), result.getString("title")
                        , result.getString("author"), result.getString("genre")
                        , result.getDate("pub_date"), result.getDouble("price")
                        , result.getString("image"));

                listData.add(bookD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<ToyData> availableBooksList;

    public void availableBooksShowListData() {
        availableBooksList = availableBooksListData();

        availablePr_col_ID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        availablePr_col_brand.setCellValueFactory(new PropertyValueFactory<>("title"));
        availablePr_col_name.setCellValueFactory(new PropertyValueFactory<>("author"));
        availablePr_col_cntr.setCellValueFactory(new PropertyValueFactory<>("genre"));
        availablePr_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        availablePr_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        availableProduct_tableView.setItems(availableBooksList);
    }

    public void availableBooksSelect() {
        ToyData bookD = availableProduct_tableView.getSelectionModel().getSelectedItem();
        int num = availableProduct_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        availableProduct_productID.setText(String.valueOf(bookD.getBookId()));
        availableProduct_brnd.setText(bookD.getTitle());
        availableProduct_name.setText(bookD.getAuthor());
        availableProduct_country.setText(bookD.getGenre());
        availableProduct_date.setValue(LocalDate.parse(String.valueOf(bookD.getDate())));
        availableProduct_price.setText(String.valueOf(bookD.getPrice()));

        getData.path = bookD.getImage();

        String uri = "file:" + bookD.getImage();

        image = new Image(uri, 112, 137, false, true);

        availableProduct_imageView.setImage(image);
    }

    public void availableProductSearch() {

        FilteredList<ToyData> filter = new FilteredList<>(availableBooksList, e -> true);

        availableProduct_search.textProperty().addListener((Observable, oldValue, newValue) -> {

            filter.setPredicate(predicateToyData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateToyData.getBookId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateToyData.getTitle().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateToyData.getAuthor().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateToyData.getGenre().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateToyData.getDate().toString().contains(searchKey)) {
                    return true;
                } else return predicateToyData.getPrice().toString().contains(searchKey);
            });
        });

        SortedList<ToyData> sortList = new SortedList(filter);
        sortList.comparatorProperty().bind(availableProduct_tableView.comparatorProperty());
        availableProduct_tableView.setItems(sortList);

    }

    public void purchaseAdd() {
        purchasecustomerId();

        String sql = "INSERT INTO customer (customer_id, book_id, title, author, genre, quantity, price, date) "
                + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;

            if (purchase_ProdctName.getSelectionModel().getSelectedItem() == null
                    || purchase_ProductID.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose book first");
                alert.showAndWait();
            } else {

                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(customerId));
                prepare.setString(2, purchase_info_ID.getText());
                prepare.setString(3, purchase_info_name.getText());
                prepare.setString(4, purchase_info_brand.getText());
                prepare.setString(5, purchase_info_country.getText());
                prepare.setString(6, String.valueOf(qty));

                String checkData = "SELECT title, price FROM toy WHERE title = '"
                        + purchase_ProdctName.getSelectionModel().getSelectedItem() + "'";

                double priceD = 0;

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if (result.next()) {
                    priceD = result.getDouble("price");
                }

                double totalP = (qty * priceD);

                prepare.setString(7, String.valueOf(totalP));

                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                prepare.setString(8, String.valueOf(sqlDate));

                prepare.executeUpdate();

                purchaseDisplayTotal();
                purchaseShowCustomerListData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void purchasePay() {

        String sql = "INSERT INTO customer_info (customer_id, total, date) "
                + "VALUES(?,?,?)";

        connect = database.connectDb();

        try {
            Alert alert;
            if (displayTotal == 0) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);
                alert.setContentText("Произошла ошибка");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Сообщение с подтверждением");
                alert.setHeaderText(null);
                alert.setContentText("Вы уверены?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, String.valueOf(customerId));
                    prepare.setString(2, String.valueOf(displayTotal));

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                    prepare.setString(3, String.valueOf(sqlDate));

                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Информационное сообщение");
                    alert.setHeaderText(null);
                    alert.setContentText("Успешно!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private double displayTotal;

    public void purchaseDisplayTotal() {
        purchasecustomerId();

        String sql = "SELECT SUM(price) FROM customer WHERE customer_id = '" + customerId + "'";

        connect = database.connectDb();

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                displayTotal = result.getDouble("SUM(price)");
            }

            purchase_total.setText("₽" + displayTotal);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchaseProductId() {

        String sql = "SELECT book_id FROM toy";

        connect = database.connectDb();

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("book_id"));
            }

            purchase_ProductID.setItems(listData);
            purchaseProductTitle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchaseProductTitle() {

        String sql = "SELECT book_id, title FROM toy WHERE book_id = '"
                + purchase_ProductID.getSelectionModel().getSelectedItem() + "'";

        connect = database.connectDb();

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            ObservableList listData = FXCollections.observableArrayList();

            while (result.next()) {
                listData.add(result.getString("title"));
            }

            purchase_ProdctName.setItems(listData);

            purchaseBookInfo();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void purchaseBookInfo() {

        String sql = "SELECT * FROM toy WHERE title = '"
                + purchase_ProdctName.getSelectionModel().getSelectedItem() + "'";

        connect = database.connectDb();

        String bookId = "";
        String title = "";
        String author = "";
        String genre = "";
        String date = "";

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                bookId = result.getString("book_id");
                title = result.getString("title");
                author = result.getString("author");
                genre = result.getString("genre");
                date = result.getString("pub_date");
            }

            purchase_info_ID.setText(bookId);
            purchase_info_name.setText(title);
            purchase_info_brand.setText(author);
            purchase_info_country.setText(genre);
            purchase_info_date.setText(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ObservableList<customerData> purchaseListData() {
        purchasecustomerId();
        String sql = "SELECT * FROM customer WHERE customer_id = '" + customerId + "'";

        ObservableList<customerData> listData = FXCollections.observableArrayList();

        connect = database.connectDb();

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            customerData customerD;

            while (result.next()) {
                customerD = new customerData(result.getInt("customer_id")
                        , result.getInt("book_id")
                        , result.getString("title")
                        , result.getString("author")
                        , result.getString("genre")
                        , result.getInt("quantity")
                        , result.getDouble("price")
                        , result.getDate("date"));

                listData.add(customerD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public void purchaseShowCustomerListData() {
        ObservableList<customerData> purchaseCustomerList = purchaseListData();

        purchase_col_bookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        purchase_col_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        purchase_col_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        purchase_col_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        purchase_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        purchase_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        purchase_tableView.setItems(purchaseCustomerList);

    }

    public void purchaseDisplayQTY() {
        SpinnerValueFactory<Integer> spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        purchase_quantity.setValueFactory(spinner);
    }

    private int qty;

    public void purhcaseQty() {
        qty = purchase_quantity.getValue();
    }

    private int customerId;

    public void purchasecustomerId() {

        String sql = "SELECT MAX(customer_id) FROM customer";
        int checkCID = 0;
        connect = database.connectDb();

        try {
            prepare = Objects.requireNonNull(connect).prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                customerId = result.getInt("MAX(customer_id)");
            }

            String checkData = "SELECT MAX(customer_id) FROM customer_info";

            prepare = connect.prepareStatement(checkData);
            result = prepare.executeQuery();

            if (result.next()) {
                checkCID = result.getInt("MAX(customer_id)");
            }

            if (customerId == 0) {
                customerId += 1;
            } else if (checkCID == customerId) {
                customerId = checkCID + 1;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayUsername() {
        String user = getData.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            availableProduct_form.setVisible(false);
            purchase_form.setVisible(false);

            dashboard_btn.setStyle("-fx-background-color:linear-gradient(to top right, #72513c, #ab853e);");
            availableProduct_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");

            dashboardAB();
            dashboardTI();
            dashboardTC();
            dashboardIncomeChart();
            dashboardCustomerChart();

        } else if (event.getSource() == availableProduct_btn) {
            dashboard_form.setVisible(false);
            availableProduct_form.setVisible(true);
            purchase_form.setVisible(false);

            availableProduct_btn.setStyle("-fx-background-color:linear-gradient(to top right, #72513c, #ab853e);");
            dashboard_btn.setStyle("-fx-background-color: transparent");
            purchase_btn.setStyle("-fx-background-color: transparent");

            availableBooksShowListData();
            availableProductSearch();

        } else if (event.getSource() == purchase_btn) {
            dashboard_form.setVisible(false);
            availableProduct_form.setVisible(false);
            purchase_form.setVisible(true);

            purchase_btn.setStyle("-fx-background-color:linear-gradient(to top right, #72513c, #ab853e);");
            availableProduct_btn.setStyle("-fx-background-color: transparent");
            dashboard_btn.setStyle("-fx-background-color: transparent");

            purchaseProductTitle();
            purchaseProductId();
            purchaseShowCustomerListData();
            purchaseDisplayQTY();
            purchaseDisplayTotal();

        }
    }

    private double x = 0;
    private double y = 0;

    public void logout() {
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Сообщение с подтверждением");
            alert.setHeaderText(null);
            alert.setContentText("Вы уверены, что хотите выйти из системы?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {


                logout.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("FXMLDocument.fxml")));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> stage.setOpacity(1));

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsername();

        dashboardAB();
        dashboardTI();
        dashboardTC();
        dashboardIncomeChart();
        dashboardCustomerChart();


        availableBooksShowListData();

        purchaseProductId();
        purchaseProductTitle();
        purchaseShowCustomerListData();
        purchaseDisplayQTY();
        purchaseDisplayTotal();

    }

}
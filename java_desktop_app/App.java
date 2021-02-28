import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class App
{
    public static void main(String[] args)
    {
        // Run code here!
        Form form = new Form();
        form.setVisible(true);
    }
}

class Form extends JFrame
{
    /**
     * Form class
     */
    private static final long serialVersionUID = 1L;
    public static HashMap<Integer, String> data;

    private JLabel lblInsertPerson;
    private JTextField txtName;
    private JButton btnInsertPerson, btnGetData;
    private JPanel panelData, panelButton; //, panelGetData;

    public Form()
    {
        initForm();
        initComponents();
        setVisible(true);
    }

    private void initForm()
    {
        setLayout(null);
        setTitle("Registro de Personas");
        setSize(800, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents()
    {
        lblInsertPerson = new JLabel("Nombre:");
        txtName = new JTextField();

        btnInsertPerson = new JButton("Enviar");
        btnInsertPerson.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { btnInsertPersonActionPerformed(e); }
        });
        btnGetData = new JButton("Ver todos los registros");
        btnGetData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { btnGetDataActionPerformed(e); }
        });

        panelData = new JPanel(new GridBagLayout());
        panelData.add(lblInsertPerson, new GridBagConstraints(0, 0, 1, 1, 0.1, 0.0, 
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

        panelData.add(txtName, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        
        panelButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButton.add(btnInsertPerson);
        panelButton.add(btnGetData);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelData, BorderLayout.NORTH);
        getContentPane().add(panelButton);
    }

    private void btnInsertPersonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            JOptionPane.showMessageDialog(null, "En desarrollo");
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al obtener datos de la API",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnGetDataActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            data = new HttpRequests().HttpRequestGet_getPersons();
            // String dataRequest = new HttpRequests().HttpRequestGet_getPersons();
            System.out.println(data);
            // System.out.println(dataRequest);
            // iter data and show in JPanel ...
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Error al obtener datos de la API",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}

class HttpRequests
{
    public HttpRequests() {}

    public HashMap<Integer, String> HttpRequestGet_getPersons() throws Exception
    {
        URL url = new URL("http://127.0.0.1:5000/api/v1/get_persons");  // modificar en producción!
        
        StringBuilder result = new StringBuilder();
        // open connection (type: GET)
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line);
		}
        reader.close();
        System.out.println(result.toString());

        return stringToJson(result.toString());
    }

    private HashMap<Integer, String> stringToJson(String result)
    {

        return new HashMap<Integer, String>();
    }

    private Map<Integer, String> inputStreamToJson()
    {
        Map<String,Object> result = new ObjectMapper().readValue(JSON_SOURCE, HashMap.class);
    }
    

}


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

public class App
{
    public static void main(String[] args)
    {
        // Run code here!
        Form form = new Form();
        form.showForm();
    }
}

class Form extends JFrame
{
    /**
     * Form class
     */
    private static final long serialVersionUID = 1L;
    public static HashMap<Integer, String> data;
    public String stringData = "";

    private JLabel lblInsertPerson;
    private JTextField txtName;
    private JButton btnInsertPerson, btnGetData;
    private JPanel panelData, panelButton; //, panelGetData;

    public Form()
    {
        initForm();
        initComponents();
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

    public void showForm() {
        this.setVisible(true);
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
            if (txtName.getText().length() == 0) {
                throw new NullPointerException("Debe ingresar un nombre");
            }
            String res = new HttpRequests().HttpRequestPost_insertPersons(txtName.getText());
            System.out.println(res);
            txtName.setText("");

        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "No se pudo insertar el dato debido a un problema con el servidor",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void btnGetDataActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            data = new HttpRequests().HttpRequestGet_getPersons();
            stringData = "";

            data.forEach((k,v) -> stringData += ("Id: " + k + ", Nombre: " + v + "\n"));
            JOptionPane.showMessageDialog(
                null, stringData, "Personas registradas en el sistema:", JOptionPane.INFORMATION_MESSAGE
            );

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
        URL url = new URL("http://127.0.0.1:5000/api/v1/get_persons");  // MODIFY in deploy!
        
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

        return stringToHashMap(result.toString());
    }

    private HashMap<Integer, String> stringToHashMap(String result)
    {
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        String objects[] = result.split("}, ");
        for (String elem : objects) {
            String fields[] = elem.split(",");            

            Integer id = Integer.parseInt(fields[0].split(":")[1].split(" ")[1]);
            String name = fields[1].split(": ")[1].split("\"")[1];
            // add element to map:
            map.put(id, name);
        }
        return map;
    }

    public String HttpRequestPost_insertPersons(String name) throws Exception
    {
        // object to send in post request:
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("name", name);

        URL url = new URL("http://127.0.0.1:5000/api/v1/insert_persons");  // MODIFY in deploy!
        StringBuilder postData = new StringBuilder();

        for (Map.Entry<String, Object> param : params.entrySet())
        {
            if (postData.length() != 0) { postData.append('&'); }
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        connection.setDoOutput(true);
        connection.getOutputStream().write(postDataBytes);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        
        String resultString = "";

        for (int c = reader.read(); c != -1; c = reader.read())
            resultString += ((char) c);
		

        return resultString;
    }    
    
}


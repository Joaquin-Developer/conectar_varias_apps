import java.util.HashMap;

class Test
{
    public static void main(String[] args) {
        
        HashMap<Integer, String> map = getMap();
        // iter:
        map.forEach((k,v) -> System.out.println("Id: " + k + ", Name: " + v));

    }

    public static HashMap<Integer, String> getMap()
    {
        String line = "[{\"id\": 1, \"name\": \"Joaquín\"}, {\"idv\": 2, \"name\": \"Ana\"}, {\"idv\": 3, \"name\": \"Maria\"}]";
        HashMap<Integer, String> map = new HashMap<Integer, String>();

        String objects[] = line.split("}, ");
        for (String elem : objects) {
            String fields[] = elem.split(",");            

            Integer id = Integer.parseInt(fields[0].split(":")[1].split(" ")[1]);
            String name = fields[1].split(": ")[1].split("\"")[1];
            // add element to map:
            map.put(id, name);
        }
        return map;
    }

}
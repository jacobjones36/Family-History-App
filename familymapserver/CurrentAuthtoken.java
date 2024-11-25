package handler;

public class CurrentAuthtoken {
    static String authtoken = null;

    public String getAuthtoken() throws Exception {
        if(authtoken != null) {
            return authtoken;
        }
        else{
            throw new Exception("Error: Null AuthToken");
        }
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }
}

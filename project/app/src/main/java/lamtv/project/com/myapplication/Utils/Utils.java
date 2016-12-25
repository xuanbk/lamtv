package lamtv.project.com.myapplication.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;

public class Utils {
    public static boolean checkInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public static void saveIdLike(Context context,String id){
        SharedPreferences sharedpreferences = context.getSharedPreferences("myData",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("LIKEID",id);
        editor.commit();
    }
    public static String getIdLike(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("myData",
                Context.MODE_PRIVATE);
        return sharedpreferences.getString("LIKEID","");

    }
}

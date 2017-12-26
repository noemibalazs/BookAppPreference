package com.example.android.bookapppreference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noemi on 12/25/2017.
 */

public final class QueryUtils {

    private QueryUtils (){}

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public static List<Book> fetchDataFromBook(String requestUrl){

        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Error making HTTP request", e);
        }
        List<Book> books = extractDataFromJson(jsonResponse);

        return  books;
    }


    private static URL createUrl (String stringUrl)  {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error building Url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code"+ urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving Book JSON results", e);
        }
        finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static Bitmap makeHttpRequest(String imageUrl)throws IOException{
        Bitmap thumbnail = null;
        if(imageUrl == null){
            return thumbnail;
        }

        URL url = createUrl(imageUrl);
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                thumbnail = BitmapFactory.decodeStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error request code"+ urlConnection.getResponseCode());
            }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving Book image results", e);
        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return thumbnail;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream!=null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    private static List<Book> extractDataFromJson(String bookJSON){
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }
        List<Book> books = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(bookJSON);
            JSONArray bookArray = root.getJSONArray("items");

            for (int i = 0; i<bookArray.length(); i++){

                String author= "";
                double price;
                JSONObject featureBook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = featureBook.getJSONObject("volumeInfo");
                JSONArray authors = volumeInfo.optJSONArray("authors");
                if(authors!=null){
                    if(authors.length()>1){
                        for(int j=0; j<authors.length();j++){
                            author+= authors.getString(j);
                            author+="";
                        }
                    }
                    else {
                        author=authors.getString(0);
                    }
                }

                String title = volumeInfo.getString("title");
                JSONObject searchInfo = featureBook.optJSONObject("searchInfo");
                String description = volumeInfo.optString("description");
                if (description==null){
                    description= searchInfo.optString("textSnippet");
                }



                String url = volumeInfo.getString("infoLink");

                JSONObject imageLink = volumeInfo.getJSONObject("imageLinks");
                String imageLinkUrl = imageLink.getString("thumbnail");
                Bitmap imageUrl = makeHttpRequest(imageLinkUrl);

                JSONObject saleInfo = featureBook.getJSONObject("saleInfo");
                String saleability = saleInfo.getString("saleability");
                if (saleability.equals("NOT FOR SALE") || saleInfo.optJSONObject("retailPrice")==null){
                    price = 0.0;
                }
                else {
                    price = saleInfo.optJSONObject("retailPrice").getDouble("amount");
                }

                String published = volumeInfo.optString("publishedDate");

                Book book = new Book(author, title, description, imageUrl, price, url, published);
                books.add(book);

            }

        }
        catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing Json results", e);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }




}

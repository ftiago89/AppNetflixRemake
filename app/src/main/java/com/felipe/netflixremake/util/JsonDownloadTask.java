package com.felipe.netflixremake.util;

import android.content.Context;
import android.os.AsyncTask;

import com.felipe.netflixremake.model.Category;
import com.felipe.netflixremake.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class JsonDownloadTask extends AsyncTask<String, Void, List<Category>> {

    private final Context context;

    public JsonDownloadTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<Category> doInBackground(String... params) {
        String url = params[0];

        try {
            URL requestUrl = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode > 400) {
                throw new IOException("Erro na comunicação com o servidor");
            }

            BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());

            String jsonAsString = toString(in);
            JSONObject teste = new JSONObject(jsonAsString);

            return getCategories(teste);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
    }

    private List<Category> getCategories(JSONObject json) throws JSONException {
        List<Category> categories = new ArrayList<>();

        JSONArray categoryArray = json.getJSONArray("category");
        for (int i = 0; i < categoryArray.length(); i++) {
            JSONObject category = categoryArray.getJSONObject(i);
            String title = category.getString("title");

            List<Movie> movies = new ArrayList<>();
            JSONArray movieArray = category.getJSONArray("movie");
            for (int j = 0; j < movieArray.length(); j++) {
                JSONObject movie = movieArray.getJSONObject(j);
                Movie movieObj = new Movie();
                movieObj.setCoverUrl(movie.getString("cover_url"));

                movies.add(movieObj);
            }

            Category categoryObj = new Category();
            categoryObj.setName(title);
            categoryObj.setMovies(movies);

            categories.add(categoryObj);
        }

        return categories;
    }

    private String toString(InputStream in) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = in.read(bytes)) > 0) {
            baos.write(bytes, 0, lidos);
        }

        return baos.toString();
    }
}

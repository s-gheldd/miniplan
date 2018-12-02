package de.sauroter.miniplan.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CheckUserCredentialsTask extends AsyncTask<Uri, Integer, CheckUserCredentialsTask.Response> {

    public static Response SUCCESS = new Response(true, HttpsURLConnection.HTTP_OK);
    private final ProgressBar _progress_bar;


    public CheckUserCredentialsTask(final ProgressBar _progress_bar) {
        this._progress_bar = _progress_bar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _progress_bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Response doInBackground(Uri... uris) {


        final Uri uri = new Uri.Builder().scheme("https")
                .authority("majo-minis.de")
                .path("App/Handy.php")
                .appendQueryParameter("Name", "Test")
                .appendQueryParameter("Passwort", "Lampe")
                .build();


        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) new URL(uri.toString()).openConnection();
            final int responseCode = urlConnection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                return new Response(false, responseCode);
            }


            final String response = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())).readLine();
            final String[] split = TextUtils.split(response, "%");
            int count = 0;
            for (String s : split) {
                System.out.println(s);
                count++;
                publishProgress((int) ((float) count / split.length * 100));
                Thread.sleep(100);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return SUCCESS;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        _progress_bar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        _progress_bar.setVisibility(View.GONE);
    }

    public static class Response {

        private final boolean success;

        private final int code;

        private Response(boolean success, int code) {
            this.success = success;
            this.code = code;
        }

        public boolean isSuccess() {
            return success;
        }

        public int getCode() {
            return code;
        }
    }
}

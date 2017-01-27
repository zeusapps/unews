package ua.in.zeusapps.ukrainenews.services;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ua.in.zeusapps.ukrainenews.R;

public class Formatter {
    private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private final SimpleDateFormat _formatterUTC;
    private final String _htmlTemplate;

    public Formatter(Context context){
        _formatterUTC = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        _formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC"));

        _htmlTemplate = readTemplate(context);
    }

    public String formatDate(String dateTime){
        try{
            Date date = _formatterUTC.parse(dateTime);
            return SimpleDateFormat.getDateTimeInstance().format(date);
        }
        catch (ParseException e){
            return "";
        }
    }

    public String formatHtml(String html){
        return String.format(_htmlTemplate, html);
    }

    @NonNull
    private String readTemplate(Context context){
        InputStream stream = context.getResources().openRawResource(R.raw.article_template);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}

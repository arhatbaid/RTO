package rto.example.com.rto.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by robby on 8/1/17.
 */

public class AppHelper {
    public static boolean CheckEditText(EditText editText) {
        if (editText.getText().toString().trim().equals("")) {
            editText.setError("*");
            //YoYo.with(Techniques.Shake).duration(400).playOn(editText);
            return false;
        } else {
            editText.setError(null);
        }
        return true;
    }

    public static boolean isPermissionGranted(String permission, Context context) {
        int result = ContextCompat.checkSelfPermission(context, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    public static boolean IsValidEmailAddress(final String hex) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static void ShowToast(Context c, String msString) {
        Toast.makeText(c, msString, Toast.LENGTH_SHORT).show();
    }


    public static boolean isNetConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        // context.startActivity(new Intent(context, ActInternetError.class));
        return false;
    }

    public static int getRandomNumber() {
        Random rand = new Random();
        return 0 + rand.nextInt((10000 - 0) + 1);
    }

    public static Bitmap setPic(String path, boolean isIdProof, ImageView imgPancard, ImageView imgIdProof) {
        int targetW = 800;
        int targetH = 450;

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path.replace("file:/", ""), bmOptions);

        String encodedimg = AppHelper.encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 40);
//        Prefs.putString(PrefsKeys.profile, encodedimg);
//        databaseReference.child(Constants.USERS).child(Prefs.getString(PrefsKeys.uid, "")).child(Constants.PROFILE_PICTURE_URL).setValue(encodedimg);
        if (isIdProof)
            imgIdProof.setImageBitmap(bitmap);
        else imgPancard.setImageBitmap(bitmap);


        return bitmap;
    }

    public static Uri getImageUrlWithAuthority(Uri uri, Context context) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(bmp, context);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Bitmap inImage, Context context) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getpathFromURI(Uri selectedImage, Context context) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();
        return imgDecodableString;
    }


    public static boolean validatePancard(String s) {
        if (s.length() != 10) {
            return false;
        }
        if (!s.substring(0, 5).matches("[a-zA-Z]+")) {
            return false;
        }

        if (!s.substring(5, 9).matches("[0-9]+")) {
            return false;
        }

        if (!s.substring(9).matches("[a-zA-Z]+")) {
            return false;
        }
        return true;
    }

    public static String getDateFormated(String date) {
        String[] fromatedDate = date.split("-");
        return fromatedDate[2] + "-" + convertMonth(Integer.parseInt(fromatedDate[1]));
    }

    public static String convertMonth(int month) {
        String[] str = {"Jan",
                "Feb",
                "March",
                "April",
                "May",
                "June",
                "July",
                "Aug",
                "Sept",
                "Oct",
                "Nov",
                "Dec"};
        return str[month];

    }

    public static boolean isValidString(String txt) {
        if (txt != null && !txt.isEmpty())
            return true;
        else
            return false;
    }

    public static AlertDialog showAlertDialog(Context context, String msg, String title) {
        AlertDialog alert11 = null;
        try {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
            builder1.setMessage(msg);
            builder1.setTitle(title);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            alert11 = builder1.create();
            alert11.show();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("AlertException", e.getMessage());
        }
        return alert11;
    }



}

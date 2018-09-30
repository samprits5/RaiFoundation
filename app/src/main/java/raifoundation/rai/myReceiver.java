package raifoundation.rai;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import static android.content.Context.DOWNLOAD_SERVICE;

public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(MainActivity.enq);
            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            Cursor c = dm.query(q);
            if (c.moveToFirst()) {
                int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                    String PATH = Environment.getExternalStorageDirectory()+"/Download/"+"rai.apk";
                    File apkFile = new File(PATH);
                    Intent intent2 = new Intent(Intent.ACTION_VIEW);
                    intent2.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);

                }
            }
        }
    }
}

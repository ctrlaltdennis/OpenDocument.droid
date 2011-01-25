
package at.tomtasche.reader.background;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import openoffice.IllegalMimeTypeException;
import openoffice.MimeTypeNotFoundException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import at.tomtasche.reader.R;
import at.tomtasche.reader.background.openoffice.JOpenDocumentWrapper;
import at.tomtasche.reader.ui.OfficeInterface;

public class DocumentLoader extends Handler implements DocumentInterface, OfficeInterface {

    public static DocumentLoader getThreadedLoader(final OfficeInterface office) {
        final HandlerThread thread = new HandlerThread("DocumentLoader");
        thread.start();

        return new DocumentLoader(thread.getLooper(), office);
    }

    private JOpenDocumentWrapper tschopen;

    private final OfficeInterface office;

    private DocumentLoader(final Looper looper, final OfficeInterface office) {
        super(looper);

        this.office = office;
    }

    public void loadDocument(final InputStream stream, final File cache) throws Exception {
        try {
            tschopen = new JOpenDocumentWrapper(this, stream, cache);
        } catch (final MimeTypeNotFoundException e) {
            e.printStackTrace();

            showToast(R.string.toast_error_open_file);
        } catch (final IllegalMimeTypeException e) {
            e.printStackTrace();

            showToast(R.string.toast_error_open_file);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();

            showToast(R.string.toast_error_find_file);
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();

            showToast(R.string.toast_error_illegal_file);
        } catch (final OutOfMemoryError e) {
            e.printStackTrace();

            showToast(R.string.toast_error_out_of_memory);
        }
    }

    @Override
    public int getPageCount() {
        if (tschopen == null) {
            return 0;
        }
        return tschopen.getPageCount();
    }

    @Override
    public int getPageIndex() {
        if (tschopen == null) {
            return 0;
        }
        return tschopen.getPageIndex();
    }

    @Override
    public void showProgress() {
        office.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                office.showProgress();
            }
        });
    }

    @Override
    public void hideProgress() {
        office.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                office.hideProgress();
            }
        });
    }

    @Override
    public void showDocument(final String html) {
        office.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                office.showDocument(html);
            }
        });
    }

    @Override
    public void showToast(final int resId) {
        office.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                office.showToast(resId);
            }
        });
    }

    @Override
    public void runOnUiThread(final Runnable runnable) {
        office.runOnUiThread(runnable);
    }

    @Override
    public boolean hasNext() {
        if (tschopen == null) {
            return false;
        }

        return tschopen.hasNext();
    }

    @Override
    public void getNext() {
        if (tschopen == null) {
            return;
        }

        post(new Runnable() {

            @Override
            public void run() {
                tschopen.getNext();
            }
        });
    }

    @Override
    public boolean hasPrevious() {
        if (tschopen == null) {
            return false;
        }

        return tschopen.hasPrevious();
    }

    @Override
    public void getPrevious() {
        if (tschopen == null) {
            return;
        }

        post(new Runnable() {

            @Override
            public void run() {
                tschopen.getPrevious();
            }
        });
    }
}

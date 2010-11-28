
package at.tomtasche.reader.odt;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import openOffice.html.URITranslator;

public class AndroidImageUriTranslator implements URITranslator {

    @Override
    public URI translate(final URI source) {
        final File imageFile = new File(source);
        final String imageFileName = imageFile.getName();

        URI result = null;

        try {
            result = new URI("content://at.tomtasche.reader/" + imageFileName);
        } catch (final URISyntaxException e) {
            e.printStackTrace();
        }

        return result;
    }
}

package onlinemarket.util;

import java.util.ArrayList;

public class MimeTypesImage {
	public static final String MIME_IMAGE_BMP = "image/bmp";
	public static final String MIME_IMAGE_GIF = "image/gif";
	public static final String MIME_IMAGE_JPEG = "image/jpeg";
	public static final String MIME_IMAGE_PNG = "image/png";
	public static final String MIME_APPLICATION_OCTET_STREAM = "application/octet-stream";

	public static ArrayList<String> mimeTypeMapping;

	static {
		mimeTypeMapping = new ArrayList<String>(6) {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			{
				add(MIME_IMAGE_BMP);
				add(MIME_IMAGE_GIF);
				add(MIME_IMAGE_JPEG);
				add(MIME_IMAGE_JPEG);
				add(MIME_IMAGE_JPEG);
				add(MIME_IMAGE_PNG);
			}
		};

	};

	/**
	 * Returns the corresponding MIME type to the given extension. If no MIME type
	 * was found it returns 'application/octet-stream' type.
	 */
	public static String getMimeType(String ext) {
		boolean flag = lookupMimeType(ext);
		if (!flag) {
			ext = MIME_APPLICATION_OCTET_STREAM;
		}
		return ext;
	}

	/**
	 * Simply returns MIME type or <code>null</code> if no type is found.
	 */
	public static boolean lookupMimeType(String ext) {
		return mimeTypeMapping.contains(ext.toLowerCase());
	}

}

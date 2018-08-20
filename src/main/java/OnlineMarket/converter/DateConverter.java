package OnlineMarket.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		sf.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (!source.isEmpty()) {
			try {
				return sf.parse(source);
			} catch (ParseException e) {

			}
		}
		return null;
	}
}
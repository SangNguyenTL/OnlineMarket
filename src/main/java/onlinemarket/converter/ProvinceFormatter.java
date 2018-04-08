package onlinemarket.converter;

import java.util.Locale;

import org.springframework.format.Formatter;

import onlinemarket.model.Province;

public class ProvinceFormatter implements Formatter<Province>{

	@Override
	public String print(Province arg0, Locale arg1) {
		if(arg0 == null) return null;
		return Integer.toString(arg0.getId());
	}

	@Override
	public Province parse(String arg0, Locale arg1){
		try {
			Integer id = Integer.parseInt(String.valueOf(arg0));
			return new Province(id);
		} catch (NumberFormatException e) {
			return null;
		}
	}

}

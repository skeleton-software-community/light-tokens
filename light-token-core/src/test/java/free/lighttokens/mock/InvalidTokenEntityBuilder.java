package free.lighttokens.mock;

import free.lightokens.api.builder.EntityBuilder;
import free.lightokens.api.model.entity.InvalidLToken;
import free.lighttokens.core.model.InvalidLTokenImpl;

public class InvalidTokenEntityBuilder implements EntityBuilder<InvalidLToken>{

	@Override
	public InvalidLToken instantiate() {
		return new InvalidLTokenImpl();
	}

}

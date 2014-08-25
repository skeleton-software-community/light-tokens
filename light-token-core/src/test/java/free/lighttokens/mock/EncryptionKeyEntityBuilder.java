package free.lighttokens.mock;

import free.lightokens.api.builder.EntityBuilder;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.model.EncryptionKeyImpl;

public class EncryptionKeyEntityBuilder implements EntityBuilder<EncryptionKey>{

	@Override
	public EncryptionKey instantiate() {
		return new EncryptionKeyImpl();
	}

}

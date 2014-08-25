package free.lighttokens.core.factory;

import free.lightokens.api.builder.EntityBuilder;
import free.lightokens.api.components.LTokenManager;
import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.model.LTokenConfiguration;
import free.lightokens.api.model.entity.InvalidLToken;
import free.lighttokens.core.ImplementationVersion;
import free.lighttokens.core.components.LTokenManagerImpl;
import free.lighttokens.core.util.TokenSerializer;
import free.lighttokens.core.util.cipher.TokenCipher;
import free.lighttokens.core.util.cipher.TokenDecipher;
import free.lighttokens.core.util.genertor.HashGenerator;
import free.lighttokens.core.util.secret.SecretDataDecoder;
import free.lighttokens.core.util.secret.SecretDataEncoder;
import free.lighttokens.core.util.secret.SecretPartsEncoder;

public class LTokenManagerFactory {

	public static LTokenManager build(EncryptionKeyDAO keyDao, InvalidTokenDAO invalidDao, EntityBuilder<InvalidLToken> invalidBuilder,
			LTokenConfiguration config){
		
		HashGenerator hashGen = new HashGenerator();
		TokenSerializer serializer = new TokenSerializer();
		
		SecretPartsEncoder partsEncoder = new SecretPartsEncoder();
		SecretDataEncoder secretDataEncoder = new SecretDataEncoder(partsEncoder, config.getMinSecretDataLength());
		SecretDataDecoder secretDataDecoder = new SecretDataDecoder();
		
		TokenDecipher decipher = new TokenDecipher(secretDataDecoder, ImplementationVersion.IMPL_VERSION);
		TokenCipher cipher = new TokenCipher(secretDataEncoder, ImplementationVersion.IMPL_VERSION);
		return new LTokenManagerImpl(invalidBuilder, cipher, decipher, serializer, hashGen, keyDao, invalidDao, config);
	}
	
}

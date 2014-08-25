package free.lighttokens.core.factory;

import free.lightokens.api.builder.EntityBuilder;
import free.lightokens.api.components.BatchPerformer;
import free.lightokens.api.dao.EncryptionKeyDAO;
import free.lightokens.api.dao.InvalidTokenDAO;
import free.lightokens.api.model.LTokenConfiguration;
import free.lightokens.api.model.entity.EncryptionKey;
import free.lighttokens.core.components.BatchPerformerImpl;
import free.lighttokens.core.util.AlgorithmResolver;
import free.lighttokens.core.util.genertor.PhraseGenerator;

public class BatchPerformerFactory {
	
	public static BatchPerformer build(EncryptionKeyDAO keyDao, InvalidTokenDAO invalidDao, EntityBuilder<EncryptionKey> keyBuilder, 
			LTokenConfiguration config){
		
		AlgorithmResolver algoResolver = new AlgorithmResolver(config.getDefaultAlgorithm());
		PhraseGenerator phraseGen = new PhraseGenerator();
		
		return new BatchPerformerImpl(keyDao, invalidDao, keyBuilder, config, algoResolver, phraseGen);
		
	}

}

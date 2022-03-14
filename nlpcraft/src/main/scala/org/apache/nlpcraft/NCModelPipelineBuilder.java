/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nlpcraft;

import org.apache.nlpcraft.internal.util.NCResourceReader;
import org.apache.nlpcraft.nlp.token.enricher.NCEnBracketsTokenEnricher;
import org.apache.nlpcraft.nlp.token.enricher.NCEnDictionaryTokenEnricher;
import org.apache.nlpcraft.nlp.token.enricher.NCEnQuotesTokenEnricher;
import org.apache.nlpcraft.nlp.token.enricher.NCEnStopWordsTokenEnricher;
import org.apache.nlpcraft.nlp.token.enricher.NСEnSwearWordsTokenEnricher;
import org.apache.nlpcraft.nlp.token.parser.NCENOpenNLPTokenParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 *
 */
public class NCModelPipelineBuilder {
    private NCTokenParser tokParser;
    private final List<NCTokenEnricher> tokEnrichers = new ArrayList<>();
    private final List<NCEntityEnricher> entEnrichers = new ArrayList<>();
    private final List<NCEntityParser> entParsers = new ArrayList<>();
    private final List<NCTokenValidator> tokVals = new ArrayList<>();
    private final List<NCEntityValidator> entVals = new ArrayList<>();
    private Optional<NCVariantFilter> varFilter = Optional.empty();

    /**
     * @param tokEnrichers
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withTokenEnrichers(List<NCTokenEnricher> tokEnrichers) {
        Objects.requireNonNull(tokEnrichers, "List of token enrichers cannot be null.");
        tokEnrichers.forEach(p -> Objects.requireNonNull(p, "Token enricher cannot be null."));

        this.tokEnrichers.addAll(tokEnrichers);

        return this;
    }

    /**
     * @param tokEnricher
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withTokenEnricher(NCTokenEnricher tokEnricher) {
        Objects.requireNonNull(tokEnricher, "Token enricher cannot be null.");

        this.tokEnrichers.add(tokEnricher);

        return this;
    }

    /**
     * @param entEnrichers
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withEntityEnrichers(List<NCEntityEnricher> entEnrichers) {
        Objects.requireNonNull(entEnrichers, "List of entity enrichers cannot be null.");
        entEnrichers.forEach(p -> Objects.requireNonNull(p, "Entity enrichers cannot be null."));

        this.entEnrichers.addAll(entEnrichers);

        return this;
    }

    /**
     * @param entEnricher
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withEntityEnricher(NCEntityEnricher entEnricher) {
        Objects.requireNonNull(entEnricher, "Entity enricher cannot be null.");

        this.entEnrichers.add(entEnricher);

        return this;
    }

    /**
     * @param entParsers
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withEntityParsers(List<NCEntityParser> entParsers) {
        Objects.requireNonNull(entParsers, "List of entity parsers cannot be null.");
        entParsers.forEach(p -> Objects.requireNonNull(p, "Entity parser cannot be null."));

        this.entParsers.addAll(entParsers);

        return this;
    }

    /**
     * @param entParser
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withEntityParser(NCEntityParser entParser) {
        Objects.requireNonNull(entParser, "Entity parser cannot be null.");

        this.entParsers.add(entParser);

        return this;
    }

    /**
     * @param tokVals
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withTokenValidators(List<NCTokenValidator> tokVals) {
        Objects.requireNonNull(tokVals, "List of token validators cannot be null.");
        tokVals.forEach(p -> Objects.requireNonNull(p, "Token validator cannot be null."));

        this.tokVals.addAll(tokVals);

        return this;
    }

    /**
     * @param tokVal
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withTokenValidator(NCTokenValidator tokVal) {
        Objects.requireNonNull(tokVal, "Token validator cannot be null.");

        this.tokVals.add(tokVal);

        return this;
    }

    /**
     * @param entVals
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withEntityValidators(List<NCEntityValidator> entVals) {
        Objects.requireNonNull(entVals, "List of entity validators cannot be null.");
        entVals.forEach(p -> Objects.requireNonNull(p, "Entity validators cannot be null."));

        this.entVals.addAll(entVals);

        return this;
    }

    /**
     * @param entVal
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withEntityValidator(NCEntityValidator entVal) {
        Objects.requireNonNull(entVal, "Entity validator cannot be null.");

        this.entVals.add(entVal);

        return this;
    }

    /**
     * @param varFilter
     * @return This instance for call chaining.
     */
    public NCModelPipelineBuilder withVariantFilter(NCVariantFilter varFilter) {
        this.varFilter = Optional.of(varFilter);

        return this;
    }

    /**
     *
     * @param tokParser
     * @return
     */
    public NCModelPipelineBuilder withTokenParser(NCTokenParser tokParser) {
        Objects.requireNonNull(tokParser, "Token parser cannot be null.");

        this.tokParser = tokParser;

        return this;
    }

    public NCModelPipelineBuilder withLanguage(String lang) {
        Objects.requireNonNull(lang, "Language cannot be null.");

        switch (lang.toUpperCase()) {
            case "EN":
                tokParser = new NCENOpenNLPTokenParser();

                //tokEnrichers.add(new NCEnOpenNLPLemmaPosTokenEnricher());
                //tokEnrichers.add(new NCEnStopWordsTokenEnricher());
                tokEnrichers.add(new NСEnSwearWordsTokenEnricher(NCResourceReader.getPath("badfilter/swear_words.txt")));
                tokEnrichers.add(new NCEnQuotesTokenEnricher());
                tokEnrichers.add(new NCEnDictionaryTokenEnricher());
                tokEnrichers.add(new NCEnBracketsTokenEnricher());

                this.entParsers.addAll(entParsers);

                break;
            default:
                throw new IllegalArgumentException("Unsupported language: " + lang);
        }

        return this;
    }

    /**
     * @return
     */
    public NCModelPipeline build() {
        Objects.requireNonNull(tokParser, "Token parser cannot be null.");

        return new NCModelPipeline() {
            @Override public NCTokenParser getTokenParser() {
                return tokParser;
            }
            @Override public List<NCTokenEnricher> getTokenEnrichers() {
                return tokEnrichers;
            }
            @Override public List<NCEntityEnricher> getEntityEnrichers() {
                return entEnrichers;
            }
            @Override public List<NCEntityParser> getEntityParsers() {
                return entParsers;
            }
            @Override public List<NCTokenValidator> getTokenValidators() {
                return tokVals;
            }
            @Override public List<NCEntityValidator> getEntityValidators() {
                return entVals;
            }
            @Override public Optional<NCVariantFilter> getVariantFilter() {
                return varFilter;
            }
        };
    }
}

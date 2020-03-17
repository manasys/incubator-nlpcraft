/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nlpcraft.probe.mgrs.nlp.enrichers

import java.util
import java.util.Collections

import org.apache.nlpcraft.model.{NCContext, NCElement, NCModelAdapter, NCResult}

import scala.collection.JavaConverters._
import scala.language.implicitConversions

import NCEnricherTestModel._

/**
  * Enrichers test model.
  */
class NCEnricherTestModel extends NCModelAdapter(ID, "Model enrichers test", "1.0") {
    private implicit def convert(s: String): NCResult = NCResult.text(s)

    override def getElements: util.Set[NCElement] =
        Set(
            mkElement("A", "A"),
            mkElement("B", "B"),
            mkElement("C", "C"),
            mkElement("AB", "A B"),
            mkElement("BC", "B C"),
            mkElement("ABC", "A B C"),
            mkElement("D1", "D"),
            mkElement("D2", "D")
        ).asJava

    private def mkElement(id: String, syns: String*): NCElement =
        new NCElement {
            override def getId: String = id
            override def getSynonyms: util.List[String] = syns.asJava
            override def getGroups: util.List[String] = Collections.singletonList(GROUP)
        }

    override def onContext(ctx: NCContext): NCResult =
        NCResult.text(
            NCTestSentence.serialize(ctx.getVariants.asScala.map(v ⇒ NCTestSentence(v.asScala.map(NCTestToken(_)))))
        )
}

object NCEnricherTestModel {
    final val ID = "test.enricher"

    private final val GROUP = "test-enricher-group"
}

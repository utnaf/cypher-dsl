/*
 * Copyright (c) 2019-2020 "Neo4j,"
 * Neo4j Sweden AB [https://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.cypherdsl.core;

import static org.apiguardian.api.API.Status.INTERNAL;

import org.apiguardian.api.API;
import org.neo4j.cypherdsl.core.support.Visitable;
import org.neo4j.cypherdsl.core.support.Visitor;

/**
 * A decorated statement. Used for <code>EXPLAIN</code> and <code>PROFILE</code>'d queries.
 *
 * @soundtrack Blind Guardian - Blind Guardian Live
 * @since 2020.1.2
 */
@API(status = INTERNAL, since = "2020.1.2")
class DecoratedQuery implements Statement {

	private enum Decoration implements Visitable {

		EXPLAIN, PROFILE;
	}

	private final Decoration decoration;
	private final Statement target;

	static DecoratedQuery explain(Statement target) {

		return DecoratedQuery.decorate(target, Decoration.EXPLAIN);
	}

	static DecoratedQuery profile(Statement target) {

		return DecoratedQuery.decorate(target, Decoration.PROFILE);
	}

	private static DecoratedQuery decorate(Statement target, Decoration decoration) {

		if (target instanceof DecoratedQuery) {
			throw new IllegalArgumentException("Cannot explain an already explained or profiled query.");
		}

		return new DecoratedQuery(target, decoration);
	}

	private DecoratedQuery(Statement target, Decoration decoration) {
		this.decoration = decoration;
		this.target = target;
	}

	@Override
	public void accept(Visitor visitor) {

		this.decoration.accept(visitor);
		this.target.accept(visitor);
	}
}

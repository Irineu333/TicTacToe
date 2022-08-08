package com.neo.hash.model

import com.neo.hash.util.extensions.recurring

class Intelligent(
    private val mySymbol: Hash.Symbol
) {

    private val Hash.hasCorners get() = corners.any { get(it.row, it.column) != null }
    private val Hash.hasSides get() = sides.any { get(it.row, it.column) != null }
    private val Hash.hasCenter get() = get(center.row, center.column) != null

    fun easy(hash: Hash): Hash.Block = with(hash) {
        firstRandom() ?: winOrBlock() ?: random()
    }

    fun medium(hash: Hash): Hash.Block = with(hash) {
        firstRandom() ?: blockOnSecond() ?: winOrBlock() ?: xeque(smart = false) ?: random()
    }

    fun hard(hash: Hash): Hash.Block = with(hash) {
        firstRandom() ?: blockOnSecond() ?: perfectThird() ?: winOrBlock() ?: xeque() ?: random()
    }

    /**
     * action: Best plays when I'm third to play
     * requirement: Be the third to play
     */
    private fun Hash.perfectThird(
        symbols: List<Hash.Block> = getAllSymbols()
    ): Hash.Block? {
        if (symbols.size != 2) return null

        //only corners
        if (hasCorners && !hasSides && !hasCenter) {
            return corners.filter { get(it.row, it.column) == null }.randomOrNull()
        }

        return null
    }

    /**
     * action: Blocking the first movement of opponent
     * requirement: Be the second to play
     */
    private fun Hash.blockOnSecond(
        symbols: List<Hash.Block> = getAllSymbols()
    ): Hash.Block? {

        if (symbols.size == 1) {

            if (hasCorners) {
                return center
            }

            if (hasSides) {
                val enemyBlock: Hash.Block = symbols[0]
                return (corners.filter { it.isSide(enemyBlock) } + center).random()
            }

            if (hasCenter) {
                return corners.random()
            }
        }

        return null
    }

    /**
     * action: Complete own victory or block opponent's victory
     * requirement: There are one or more segments about to be closed
     */
    private fun Hash.winOrBlock(): Hash.Block? {
        fun rows() = buildList {
            for (row in Hash.KEY_RANGE) {
                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (column in Hash.KEY_RANGE) {
                    val symbol = get(row, column)

                    if (symbol == mySymbol) {
                        myBlocks.add(
                            Hash.Block(
                                row,
                                column,
                                mySymbol
                            )
                        )
                        continue
                    }

                    if (symbol == null) {
                        emptyBlocks.add(
                            Hash.Block(
                                row,
                                column
                            )
                        )
                        continue
                    }

                    if (symbol != mySymbol) {
                        enemyBlocks.add(
                            Hash.Block(
                                row,
                                column
                            )
                        )
                    }
                }

                if (emptyBlocks.size == 1) {
                    if (myBlocks.size == 2) {
                        add(emptyBlocks[0] to myBlocks[0].symbol)
                    }
                    if (enemyBlocks.size == 2) {
                        add(emptyBlocks[0] to enemyBlocks[0].symbol)
                    }
                }
            }
        }

        fun columns() = buildList {
            for (column in Hash.KEY_RANGE) {
                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (row in Hash.KEY_RANGE) {
                    val symbol = get(row, column)

                    if (symbol == mySymbol) {
                        myBlocks.add(
                            Hash.Block(
                                row,
                                column,
                                mySymbol
                            )
                        )
                        continue
                    }

                    if (symbol == null) {
                        emptyBlocks.add(
                            Hash.Block(
                                row,
                                column
                            )
                        )
                        continue
                    }

                    if (symbol != mySymbol) {
                        enemyBlocks.add(
                            Hash.Block(
                                row,
                                column
                            )
                        )
                    }
                }

                if (emptyBlocks.size == 1) {
                    if (myBlocks.size == 2) {
                        add(emptyBlocks[0] to myBlocks[0].symbol)
                    }
                    if (enemyBlocks.size == 2) {
                        add(emptyBlocks[0] to enemyBlocks[0].symbol)
                    }
                }
            }
        }

        fun diagonal(): Pair<Hash.Block, Hash.Symbol?>? {
            val myBlocks = mutableListOf<Hash.Block>()
            val enemyBlocks = mutableListOf<Hash.Block>()
            val emptyBlocks = mutableListOf<Hash.Block>()

            for (index in Hash.KEY_RANGE) {

                val symbol = get(index, index)

                if (symbol == mySymbol) {
                    myBlocks.add(
                        Hash.Block(
                            index,
                            index,
                            mySymbol
                        )
                    )
                    continue
                }

                if (symbol == null) {
                    emptyBlocks.add(
                        Hash.Block(
                            index,
                            index
                        )
                    )
                    continue
                }

                if (symbol != mySymbol) {
                    enemyBlocks.add(
                        Hash.Block(
                            index,
                            index
                        )
                    )
                }
            }

            if (emptyBlocks.size == 1) {
                if (myBlocks.size == 2) {
                    return emptyBlocks[0] to myBlocks[0].symbol
                }
                if (enemyBlocks.size == 2) {
                    return emptyBlocks[0] to enemyBlocks[0].symbol
                }
            }

            return null
        }

        fun invertedDiagonal(): Pair<Hash.Block, Hash.Symbol?>? {
            val myBlocks = mutableListOf<Hash.Block>()
            val enemyBlocks = mutableListOf<Hash.Block>()
            val emptyBlocks = mutableListOf<Hash.Block>()

            for (column in Hash.KEY_RANGE) {

                val row = 4 - column

                val symbol = get(row, column)

                if (symbol == mySymbol) {
                    myBlocks.add(
                        Hash.Block(
                            row,
                            column,
                            mySymbol
                        )
                    )
                    continue
                }

                if (symbol == null) {
                    emptyBlocks.add(
                        Hash.Block(
                            row,
                            column
                        )
                    )
                    continue
                }

                if (symbol != mySymbol) {
                    enemyBlocks.add(
                        Hash.Block(
                            row,
                            column,
                            symbol
                        )
                    )
                }
            }

            if (emptyBlocks.size == 1) {
                if (myBlocks.size == 2) {
                    return emptyBlocks[0] to myBlocks[0].symbol
                }
                if (enemyBlocks.size == 2) {
                    return emptyBlocks[0] to enemyBlocks[0].symbol
                }
            }

            return null
        }

        val allMoves = buildList {

            addAll(rows())
            addAll(columns())

            diagonal()?.let {
                add(it)
            }

            invertedDiagonal()?.let {
                add(it)
            }
        }

        val winMove = allMoves.filter {
            it.second == mySymbol
        }.randomOrNull()?.first

        val blockMove = allMoves.filter {
            it.second != mySymbol
        }.randomOrNull()?.first

        return winMove ?: blockMove
    }

    /**
     * action: Run the first move
     * requirement: Hash is empty
     */
    private fun Hash.firstRandom(): Hash.Block? {
        if (isEmpty()) {
            return Hash.Block(
                row = Hash.KEY_RANGE.random(),
                column = Hash.KEY_RANGE.random()
            )
        }
        return null
    }

    /**
     * action: Threatens to close a segment
     * requirement: A follow-up with a piece of mine
     */
    private fun Hash.xeque(smart: Boolean = true): Hash.Block? {
        fun rows() = buildList {
            for (row in Hash.KEY_RANGE) {

                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (column in Hash.KEY_RANGE) {
                    val symbol = get(row, column)

                    if (symbol == mySymbol) {
                        myBlocks.add(
                            Hash.Block(
                                row,
                                column,
                                mySymbol
                            )
                        )
                        continue
                    }

                    if (symbol == null) {
                        emptyBlocks.add(
                            Hash.Block(
                                row,
                                column
                            )
                        )
                        continue
                    }

                    if (symbol != mySymbol) {
                        enemyBlocks.add(
                            Hash.Block(
                                row,
                                column,
                                symbol
                            )
                        )
                    }
                }

                if (
                    myBlocks.size == 1 &&
                    enemyBlocks.size == 0 &&
                    emptyBlocks.size == 2
                ) {
                    addAll(emptyBlocks)
                }
            }
        }

        fun columns() = buildList {
            for (column in Hash.KEY_RANGE) {
                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (row in Hash.KEY_RANGE) {
                    val symbol = get(row, column)

                    if (symbol == mySymbol) {
                        myBlocks.add(
                            Hash.Block(
                                row,
                                column,
                                mySymbol
                            )
                        )
                        continue
                    }

                    if (symbol == null) {
                        emptyBlocks.add(
                            Hash.Block(
                                row,
                                column
                            )
                        )
                        continue
                    }

                    if (symbol != mySymbol) {
                        enemyBlocks.add(
                            Hash.Block(
                                row,
                                column,
                                symbol
                            )
                        )
                    }
                }

                if (
                    myBlocks.size == 1 &&
                    enemyBlocks.size == 0 &&
                    emptyBlocks.size == 2
                ) {
                    addAll(emptyBlocks)
                }
            }
        }

        fun diagonal() = buildList {
            val myBlocks = mutableListOf<Hash.Block>()
            val enemyBlocks = mutableListOf<Hash.Block>()
            val emptyBlocks = mutableListOf<Hash.Block>()

            for (index in Hash.KEY_RANGE) {

                val symbol = get(index, index)

                if (symbol == mySymbol) {
                    myBlocks.add(
                        Hash.Block(
                            index,
                            index,
                            mySymbol
                        )
                    )
                    continue
                }

                if (symbol == null) {
                    emptyBlocks.add(
                        Hash.Block(
                            index,
                            index
                        )
                    )
                    continue
                }

                if (symbol != mySymbol) {
                    enemyBlocks.add(
                        Hash.Block(
                            index,
                            index,
                            symbol
                        )
                    )
                }
            }

            if (
                myBlocks.size == 1 &&
                enemyBlocks.size == 0 &&
                emptyBlocks.size == 2
            ) {
                addAll(emptyBlocks)
            }
        }

        fun invertedDiagonal() = buildList {
            val myBlocks = mutableListOf<Hash.Block>()
            val enemyBlocks = mutableListOf<Hash.Block>()
            val emptyBlocks = mutableListOf<Hash.Block>()

            for (column in Hash.KEY_RANGE) {
                val row = 4 - column

                val symbol = get(row, column)

                if (symbol == mySymbol) {
                    myBlocks.add(
                        Hash.Block(
                            row,
                            column,
                            mySymbol
                        )
                    )
                    continue
                }

                if (symbol == null) {
                    emptyBlocks.add(
                        Hash.Block(
                            row,
                            column
                        )
                    )
                    continue
                }

                if (symbol != mySymbol) {
                    enemyBlocks.add(
                        Hash.Block(
                            row,
                            column,
                            symbol
                        )
                    )
                }
            }

            if (
                myBlocks.size == 1 &&
                enemyBlocks.size == 0 &&
                emptyBlocks.size == 2
            ) {
                addAll(emptyBlocks)
            }
        }

        val moves = buildList {
            addAll(rows())
            addAll(columns())

            addAll(diagonal())
            addAll(invertedDiagonal())

        }

        return moves.let {
            if (smart) it.recurring() else it
        }.ifEmpty { moves }.randomOrNull()
    }

    /**
     * action: Make a random move
     * requirement: There is an empty block
     */
    private fun Hash.random() = getAllEmpty().filter {
        it.symbol == null
    }.random()

    companion object {
        val corners = listOf(
            Hash.Block(1, 1),
            Hash.Block(1, 3),
            Hash.Block(3, 3),
            Hash.Block(3, 1),
        )

        val sides = listOf(
            Hash.Block(1, 2),
            Hash.Block(2, 3),
            Hash.Block(2, 1),
            Hash.Block(3, 2),
        )

        val center = Hash.Block(2, 2)
    }
}
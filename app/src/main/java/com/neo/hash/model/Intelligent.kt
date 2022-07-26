package com.neo.hash.model

class Intelligent(
    private val mySymbol: Hash.Symbol
) {
    fun easy(hash: Hash): Hash.Block = with(hash) {
        return firstRandom() ?: winOrBlock() ?: xeque() ?: random()
    }

    fun medium(hash: Hash): Hash.Block = with(hash) {
        return firstRandom() ?: decisiveSecond() ?: winOrBlock() ?: xeque() ?: random()
    }

    private fun Hash.decisiveSecond(): Hash.Block? {

        var count = 0;

        for (row in Hash.KEY_RANGE) {
            for (column in Hash.KEY_RANGE) {
                if (get(row, column) != null) {
                    count++
                }
            }
        }

        if (count == 1) {
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

            val isCorners = corners.any { get(it.row, it.column) != null }
            val isSides = sides.any { get(it.row, it.column) != null }
            val isCenter = get(center.row, center.column) != null

            if (isCorners) {
                return center
            }

            if (isSides) {
                return (corners + center).random()
            }

            if (isCenter) {
                return corners.random()
            }
        }

        return null
    }

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

                val critical = emptyBlocks.size == 2 || enemyBlocks.size == 2

                if (critical && emptyBlocks.size == 1) {
                    add(emptyBlocks[0])
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

                val critical = emptyBlocks.size == 2 || enemyBlocks.size == 2

                if (critical && emptyBlocks.size == 1) {
                    add(emptyBlocks[0])
                }
            }
        }

        fun diagonal(): Hash.Block? {
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

            val critical = emptyBlocks.size == 2 || enemyBlocks.size == 2

            if (critical && emptyBlocks.size == 1) {
                return emptyBlocks[0]
            }

            return null
        }

        fun invertedDiagonal(): Hash.Block? {
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
                            column
                        )
                    )
                }
            }

            val critical = emptyBlocks.size == 2 || enemyBlocks.size == 2

            if (critical && emptyBlocks.size == 1) {
                return emptyBlocks[0]
            }

            return null
        }

        return buildList {

            addAll(rows())
            addAll(columns())

            diagonal()?.let {
                add(it)
            }

            invertedDiagonal()?.let {
                add(it)
            }
        }.randomOrNull()
    }

    private fun Hash.firstRandom(): Hash.Block? {
        if (isEmpty()) {
            return Hash.Block(
                row = Hash.KEY_RANGE.random(),
                column = Hash.KEY_RANGE.random()
            )
        }
        return null
    }

    private fun Hash.xeque(): Hash.Block? {
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
                                symbol = mySymbol
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

                if (myBlocks.size == 1 && enemyBlocks.size == 0 && emptyBlocks.size == 2) {
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
                                symbol = mySymbol
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

                if (myBlocks.size == 1 && enemyBlocks.size == 0 && emptyBlocks.size == 2) {
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
                            symbol = mySymbol
                        )
                    )
                    continue
                }

                if (symbol == null) {
                    enemyBlocks.add(
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
                            symbol = mySymbol
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

            if (
                myBlocks.size == 1 &&
                enemyBlocks.size == 0 &&
                emptyBlocks.size == 2
            ) {
                addAll(emptyBlocks)
            }
        }

        return buildList {
            addAll(rows())
            addAll(columns())

            addAll(diagonal())
            addAll(invertedDiagonal())

        }.randomOrNull()
    }

    private fun Hash.random() = buildList {
        for (row in Hash.KEY_RANGE) {
            for (column in Hash.KEY_RANGE) {
                if (get(row, column) == null) {
                    add(Hash.Block(row, column))
                }
            }
        }
    }.random()
}
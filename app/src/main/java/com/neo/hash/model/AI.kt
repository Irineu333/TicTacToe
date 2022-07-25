package com.neo.hash.model

object AI {
    fun hard(hash: Hash, mySymbol: Hash.Symbol): Hash.Block? {
        fun winOrBlock(): Hash.Block? {
            fun rows(): Hash.Block? {
                for (row in Hash.KEY_RANGE) {
                    val myBlocks = mutableListOf<Hash.Block>()
                    val enemyBlocks = mutableListOf<Hash.Block>()
                    val emptyBlocks = mutableListOf<Hash.Block>()

                    for (column in Hash.KEY_RANGE) {
                        val symbol = hash.get(row, column)

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
                }

                return null
            }

            fun columns(): Hash.Block? {
                for (column in Hash.KEY_RANGE) {
                    val myBlocks = mutableListOf<Hash.Block>()
                    val enemyBlocks = mutableListOf<Hash.Block>()
                    val emptyBlocks = mutableListOf<Hash.Block>()

                    for (row in Hash.KEY_RANGE) {
                        val symbol = hash.get(row, column)

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
                }
                return null
            }

            fun diagonal(): Hash.Block? {
                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (index in Hash.KEY_RANGE) {

                    val symbol = hash.get(index, index)

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

                    val symbol = hash.get(row, column)

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

            return rows() ?: columns() ?: diagonal() ?: invertedDiagonal()
        }

        fun first(): Hash.Block? {
            if (hash.isEmpty()) {
                val indexes = listOf(1, 2, 3)

                return Hash.Block(
                    row = indexes.random(),
                    column = indexes.random()
                )
            }
            return null
        }

        fun xeque(): Hash.Block? {
            fun rows(): Hash.Block? {
                for (row in Hash.KEY_RANGE) {

                    val myBlocks = mutableListOf<Hash.Block>()
                    val enemyBlocks = mutableListOf<Hash.Block>()
                    val emptyBlocks = mutableListOf<Hash.Block>()

                    for (column in Hash.KEY_RANGE) {
                        val symbol = hash.get(row, column)

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
                        return emptyBlocks.random()
                    }
                }

                return null
            }

            fun columns(): Hash.Block? {
                for (column in Hash.KEY_RANGE) {
                    val myBlocks = mutableListOf<Hash.Block>()
                    val enemyBlocks = mutableListOf<Hash.Block>()
                    val emptyBlocks = mutableListOf<Hash.Block>()

                    for (row in Hash.KEY_RANGE) {
                        val symbol = hash.get(row, column)

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
                        return emptyBlocks.random()
                    }
                }
                return null
            }

            fun diagonal(): Hash.Block? {
                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (index in Hash.KEY_RANGE) {

                    val symbol = hash.get(index, index)

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

                if (myBlocks.size == 1 && enemyBlocks.size == 0 && emptyBlocks.size == 2) {
                    return emptyBlocks.random()
                }

                return null
            }

            fun invertedDiagonal(): Hash.Block? {
                val myBlocks = mutableListOf<Hash.Block>()
                val enemyBlocks = mutableListOf<Hash.Block>()
                val emptyBlocks = mutableListOf<Hash.Block>()

                for (column in Hash.KEY_RANGE) {
                    val row = 4 - column

                    val symbol = hash.get(row, column)

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
                    return emptyBlocks.random()
                }

                return null
            }

            return rows() ?: columns() ?: diagonal() ?: invertedDiagonal()
        }

        fun random() = buildList {
            for (row in Hash.KEY_RANGE) {
                for (column in Hash.KEY_RANGE) {
                    if (hash.get(row, column) == null) {
                        add(Hash.Block(row, column))
                    }
                }
            }
        }.randomOrNull()

        return first() ?: winOrBlock() ?: xeque() ?: random()
    }
}
# Tree Transformer
#### [original task](https://gist.github.com/mmazurkevich/cff4726d088589e6990088000fbe210f#tree-transformer)

Create a program that accepts 2 general trees as an input and calculates the list of transformations required to get the second tree from the first.

Only 2 possible operations are allowed, ADD and REMOVE, and both are only applicable to leaves. The ADD operation creates a new tree node. For example, ADD(1, 5) creates a node with ID 5 as a leaf of the node with ID 1. The REMOVE operation deletes nodes from the tree.

Note: We cannot remove a node if it has at least 1 leaf, and the order of the node’s children is not important.

The program will accept 2 files with the tree’s edge lists as an input and then print the list of operations as an output.

# Example:
Example file content:
[1, 9][9, 8][1, 6][6, 5][6, 2][1, 7]

Example input:
/foo/one.txt /foo/two.txt
Example output:
REMOVE(3), REMOVE(2), REMOVE(6), ADD(1, 6), ADD(1, 3)

Content of file one.txt
[1, 2][2, 3][1, 4][4, 5][4, 6]

Content of file two.txt
[1,6][1, 3][1, 4][4, 5]

# How to run
- `javac TreeTransformations.java`
- `java TreeTransformations 01.txt 02.txt`
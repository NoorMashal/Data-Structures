package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 * 
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) { 
        fileName = f; 
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        StdIn.setFile(fileName);
        ArrayList<CharFreq> list = new ArrayList<CharFreq>();
        int count = 0;
        int[] b = new int[128];
        while (StdIn.hasNextChar())
        {
            char newChar = StdIn.readChar();
            b[(int)newChar]+=1;
            count++;
        }
        for(int i = 0; i < 128; i++)
        {
            if(b[i] > 0)
            {
                double frequency = (double)b[i]/(double)count;
                CharFreq x = new CharFreq((char)i, frequency);
                list.add(x);
            }
        }
        if (list.size() == 1)
        {   
            if ((int)list.get(0).getCharacter() == 127)
            {
                list.add(new CharFreq((char)0, 0));
            }
            else
            {
                char xtra = (char)((int)list.get(0).getCharacter()+1);
                list.add(new CharFreq(xtra, 0));
            }
        }
        Collections.sort(list);
        sortedCharFreqList = list;
    /* Your code goes here */
    }

    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    public void makeTree() {
        Queue<TreeNode> source = new Queue<TreeNode>();
        Queue<TreeNode> target = new Queue<TreeNode>();

        for(int i = 0; i < sortedCharFreqList.size(); i++)
        {
            source.enqueue(new TreeNode(sortedCharFreqList.get(i),null,null));
        }
        TreeNode left = null;
        TreeNode right = null;

        while ( !source.isEmpty() || target.size() != 1)
        {
            if(target.isEmpty() == true)
            {
                left = source.dequeue();
            }
            else if(source.isEmpty() == true)
            {
                left = target.dequeue();
            }
            else if( target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
            {
                left = target.dequeue();
            }
            else left = source.dequeue();
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
            if(target.isEmpty() == true)
            {
                right = source.dequeue();
            }
            else if(source.isEmpty() == true)
            {
                right = target.dequeue();
            }
            else if( target.peek().getData().getProbOcc() < source.peek().getData().getProbOcc())
            {
                right = target.dequeue();
            }
            else right = source.dequeue();
            
                CharFreq c = new CharFreq(null, left.getData().getProbOcc() + right.getData().getProbOcc());
                target.enqueue(new TreeNode(c,left,right));
        }
        huffmanRoot = target.peek();


    /*  Your code goes here */
    }
    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() 
    {
        String[] encoding = new String[128];
        encodings = look(huffmanRoot, "", encoding);
        encodings = encoding;

	/* Your code goes here */ // DONE
    }
    
    private String[] look(TreeNode root, String s, String[] encodes) // A method to assign "1" & "0" to each node while traversing using recursion
    {
        TreeNode curr = root;
        if(curr != null)
        {
            look(curr.getLeft(),s+"0",encodes);
            look(curr.getRight(),s+"1",encodes);
            
            if(curr.getRight() == null && curr.getLeft() == null) encodes[(int)curr.getData().getCharacter()] = s;
            return encodes;
        }
        else
        {
            s = s.substring(0, s.length()-2);
            return null;
        }
    }
    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     * 
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) 
    {
        StdIn.setFile(fileName);
        String compress = "";
        while(StdIn.hasNextChar() == true)
        {
            int a = StdIn.readChar();
            compress += encodings[a];
        }
        writeBitString(encodedFile, compress);

	/* Your code goes here */ // DONE
    }
    
    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     * 
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;
            
            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }
        
        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method 
     * to convert the file into a bit string, then decodes the bit string using the 
     * tree, and writes it to a decoded file. 
     * 
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) 
    {
        StdOut.setFile(decodedFile); // creates the file
        String input = readBitString(encodedFile); // translates the encoding to string
        decodedFile = ""; // makes an empty space in the file
        while(input.length() != 0)
        {
            TreeNode walker = huffmanRoot; //walks thru the tree
            int chara = 0; // TELLS YOU WHICH CHARACTER TO LOOK AT AND TO HELP TRAVERSE THRU STRING | AND ALSO STARTS AT 0 TO LOOK AT STRING FROM BEGINNING EVERY ITTERATION 
            while(walker.getData().getCharacter() == null)
            {
            if (input.substring(chara,chara+1).equals("1"))
            {
                walker = walker.getRight();
            }
            else 
            {
                walker = walker.getLeft();
            }
            chara++;
        }
            decodedFile += walker.getData().getCharacter(); // adds the character to the file
            input = input.substring(chara); //gets rid of the traveresed string/characters in the string to start with a new smaller string
        }
        StdOut.print(decodedFile);
	/* Your code goes here */ // DONE
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     * 
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";
        
        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();
            
            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString + 
                String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }
            
            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver. 
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() { 
        return fileName; 
    }

    public ArrayList<CharFreq> getSortedCharFreqList() { 
        return sortedCharFreqList; 
    }

    public TreeNode getHuffmanRoot() { 
        return huffmanRoot; 
    }

    public String[] getEncodings() { 
        return encodings; 
    }
}

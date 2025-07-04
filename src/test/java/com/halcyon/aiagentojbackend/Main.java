package com.halcyon.aiagentojbackend;

import java.util.Scanner;

// 注意类名必须为 Main, 不要有任何 package xxx 信息
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        String s = in.next();
        int[] count = new int[2];
        for(int i = 0;i < n;i++){
            if(s.charAt(i) == '0') count[0]++;
            else count[1]++;
        }
        int x = n - k;
        int a = x / 2;  //拿1
        int b = x - a;  //拿0
        if(count[0] < b){
            a += b - count[0];
            b = count[0];
        }
        if(count[1] < a){
            b += a - count[1];
            a = count[1];
        }
        StringBuilder sb = new StringBuilder(s);
        int start = 0;
        while(a > 0){
            if(sb.charAt(start) == '1'){
                sb.delete(start,start+1);
                a--;
            }else{
                start++;
            }
        }
        start = 0;
        while(b > 0){
            if(sb.charAt(start) == '0'){
                sb.delete(start,start+1);
                b--;
            }else{
                start++;
            }
        }
        System.out.println(sb.toString());
    }
}
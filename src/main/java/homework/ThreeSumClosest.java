package homework;

import java.util.Scanner;
import java.util.Arrays;

public class ThreeSumClosest {
    public static int threeSumClosest(int[] nums, int target) {
        int n = nums.length, ans = 0;
        // TODO begin
        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {
            int start = i + 1, end = n - 1;
            while (start < end) {
                int sum = nums[i] + nums[start] + nums[end];
                if (Math.abs(target - sum) < Math.abs(target - ans)) {
                    ans = sum;
                }
                if (sum > target) {
                    end--;
                } else if (sum < target) {
                    start++;
                } else {
                    return sum;
                }
            }
        }
        // TODO end
        return ans;
    }
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int size;
        System.out.print("Enter the size of the array: ");
        size = input.nextInt();
        int[] nums = new int[size];
        System.out.print("Enter the array, separated by a space: ");
        for (int i = 0; i < nums.length; i++) {
            nums[i] = input.nextInt();
        }
        System.out.print("Enter the target: ");
        int target = input.nextInt();
        System.out.println(threeSumClosest(nums, target));
    }
}

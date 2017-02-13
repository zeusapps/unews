package ua.in.zeusapps.ukrainenews.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

public class FragmentHelper {

    private FragmentManager fragmentManager;
    private int container;

    public FragmentHelper(FragmentManager fragmentManager, int container) {
        this.fragmentManager = fragmentManager;
        this.container = container;
    }

    public static void updateFragment(FragmentManager fragmentManager) {
        Fragment currentFragment = getVisibleFragment(fragmentManager);
        if (currentFragment == null) {
            return;
        }

        FragmentTransaction fragTransaction = fragmentManager.beginTransaction();
        fragTransaction.detach(currentFragment);
        fragTransaction.attach(currentFragment);
        fragTransaction.commitAllowingStateLoss();
    }

    public static Fragment getVisibleFragment(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            List<Fragment> fragments = fragmentManager.getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment != null && fragment.isVisible())
                        return fragment;
                }
            }
        }
        return null;
    }

    public static List<Fragment> getFragments(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            return fragmentManager.getFragments();
        }
        return null;
    }

    public static boolean contains(FragmentManager fragmentManager, Fragment fragment) {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f != null) {
                    if (((Object) f).getClass().isAssignableFrom(((Object) fragment).getClass())) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    public static void add(FragmentManager fragmentManager, Fragment fragment, int container) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null).replace(container,
                fragment).commitAllowingStateLoss();
    }

    public static void add(FragmentManager fragmentManager, Fragment fragment, int container, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null).replace(container,
                fragment, tag).commitAllowingStateLoss();
    }

    public static void replace(FragmentManager fragmentManager, Fragment fragment, int container) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment).commit();
    }

    public static void replace(FragmentManager fragmentManager, Fragment fragment, int container, String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment, tag).commit();
    }


    /**
     * @param fragmentManager
     * @return true when last fragment was popped or when fragmentManager has no fragments to pop
     */
    public static boolean pop(FragmentManager fragmentManager) {
        fragmentManager.popBackStack();
        return getStackCount(fragmentManager) == 0;
    }

    /**
     * @param fragmentManager
     * @param saveCount
     * @return true if saveCount <= fragmentManager.getBackStackEntryCount
     */
    public static boolean pop(FragmentManager fragmentManager, int saveCount) {
        int backStackCount = getStackCount(fragmentManager);
        if (backStackCount > saveCount) {
            fragmentManager.popBackStack();
            return (backStackCount - 1) <= saveCount;
        } else {
            return true;
        }
    }

    public static void popImmediate(FragmentManager fragmentManager) {
        fragmentManager.popBackStackImmediate();
    }

    public static void clearStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void clearStack(FragmentManager fragmentManager, int saveCount) {
        int backStackCount = getStackCount(fragmentManager);
        while (backStackCount > saveCount) {
            popImmediate(fragmentManager);
            backStackCount--;
        }
    }

    public static int getStackCount(FragmentManager fragmentManager) {
        return fragmentManager.getBackStackEntryCount();
    }

    public void add(Fragment fragment) {
        fragmentManager.beginTransaction().addToBackStack(null).replace(container,
                fragment).commit();
    }

    public void replace(Fragment fragment) {
        fragmentManager.beginTransaction().replace(container,
                fragment).commit();
    }
}

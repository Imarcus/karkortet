package imarcus.android.karkortet;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/*
 * Adapter for 
 */
class SlidePageAdapter extends FragmentPagerAdapter {
  private List<Fragment> fragments;

  public SlidePageAdapter(FragmentManager fm, List<Fragment> fragments) {
    super(fm);
    this.fragments = fragments;
  }
  @Override
  public Fragment getItem(int position) {
    return this.fragments.get(position);
  }
  
  @Override
  public int getCount() {
    return this.fragments.size();
  }
}


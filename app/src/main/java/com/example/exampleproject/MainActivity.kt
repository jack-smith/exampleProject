package com.example.exampleproject

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.example.exampleproject.base.BaseMvvmActivity
import com.example.exampleproject.databinding.ActivityExampleBinding
import com.example.exampleproject.model.SongModel
import com.example.exampleproject.viewmodel.SongViewModel

class MainActivity : BaseMvvmActivity<ActivityExampleBinding, SongViewModel>() {
    private lateinit var baseQuickAdapter: BaseQuickAdapter<SongModel, QuickViewHolder>

    override fun initViewModel() {
        mViewModel = getActivityScopeViewModel(SongViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_example
    }

    override fun initObservable() {
        super.initObservable()
        mViewModel.songList.observe(this) {
            baseQuickAdapter.submitList(it)
            if (it.isEmpty())
                baseQuickAdapter.isEmptyViewEnable = true
        }
    }

    override fun initListener() {
        super.initListener()
        mBinding.run {
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val searchContent = if (TextUtils.isEmpty(s.toString())) "歌" else s.toString()
                    mViewModel.search(searchContent)
                }

            })

            rbShutdownSorting.setOnClickListener {
                    rbShutdownSorting.isChecked = true
                    rbSortByPrice.isChecked = false
                    mViewModel.sortingList(false)
            }

            rbSortByPrice.setOnClickListener {
                    rbSortByPrice.isChecked = true
                    rbShutdownSorting.isChecked = false
                    mViewModel.sortingList(true)
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getSongList()
    }

    override fun initView() {
        super.initView()
        mBinding.run {
            (rvSong.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            rvSong.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            rvSong.adapter = object : BaseQuickAdapter<SongModel, QuickViewHolder>() {
                override fun onBindViewHolder(
                    holder: QuickViewHolder,
                    position: Int,
                    item: SongModel?
                ) {
                    item?.run {
                        holder.setText(R.id.tv_artist_name, artistName)
                            .setText(R.id.tv_track_name, trackName)
                            .setText(R.id.tv_price, "$${trackPrice}")
                        Glide.with(context).load(artworkUrl100).into(holder.getView(R.id.iv_cover))
                    }

                }

                override fun onCreateViewHolder(
                    context: Context,
                    parent: ViewGroup,
                    viewType: Int
                ): QuickViewHolder {
                    return QuickViewHolder(R.layout.item_song_list, parent)
                }

            }.apply {
                baseQuickAdapter = this
            }
        }
    }

    inner class ClickProxy  {
        fun search() {
            mViewModel
        }
    }
}

/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExampleProjectTheme {
        Greeting("Android")
    }
}*/

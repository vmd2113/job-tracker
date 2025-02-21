package com.duongw.commonservice.repository.search;

import com.duongw.common.model.dto.response.PageResponse;
import com.duongw.common.utils.PageResponseConverter;
import com.duongw.commonservice.model.dto.response.item.ItemResponseDTO;
import com.duongw.commonservice.model.entity.Item;
import com.duongw.commonservice.repository.ItemRepository;
import com.duongw.commonservice.repository.search.specification.ItemSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class ItemSearchRepository {
    private final ItemRepository itemRepository;

    public PageResponse<ItemResponseDTO> searchItems(String itemName, String itemCode, String categoryCode, int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        int pageAdjust = pageNo > 1 ? pageNo - 1 : 0;

        PageRequest pageRequest = PageRequest.of(pageAdjust, pageSize, sort);

        Page<Item> itemPage = itemRepository.findAll(
                ItemSpecification.searchByMultipleFields(itemName, itemCode, categoryCode),
                pageRequest
        );

        Page<ItemResponseDTO> itemResponsePage = itemPage.map(item -> ItemResponseDTO.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .itemCode(item.getItemCode())
                .itemValue(item.getItemValue())
                .parentItemId(item.getParentItemId())
                .categoryId(item.getCategory().getCategoryId())
                .categoryCode(item.getCategory().getCategoryCode())
                .status(item.getStatus())
                .build());


        return PageResponseConverter.convertFromPage(itemResponsePage);
    }




}
